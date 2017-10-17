package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private static final String newLine = System.getProperty("line.separator");
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			DataOutputStream dos = new DataOutputStream(out);
			String line = br.readLine();
			String requestType = HttpRequestUtils.getRequestType(line);
			String requestUrl = HttpRequestUtils.getRequestUrl(line);
			log.debug("request Url : " + requestUrl);
			int length = 0;
			boolean loginChecked = false;

			while (!line.equals("")) {
				line = br.readLine();
				if (line.contains("Content-Length")) {
					length = Integer.parseInt(line.split(" ")[1]);
				}
				if(line.contains("Cookie")){
					loginChecked = Boolean.valueOf(line.split(" ")[1].split("=")[1]).booleanValue();
				}
				log.debug(line);
			}

			try {
				requestHandlling(requestType, dos, requestUrl, br, length, loginChecked);
			} catch (IOException e) {
				response404Header(dos);
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void loginMiddleWare(DataOutputStream dos, Boolean loginChecked) {
		if(loginChecked) {
			log.debug("exit");
			return;
		}
		response302Header(dos, "user/login.html");
	}
	private void requestHandlling(String requestType, DataOutputStream dos, String requestUrl, BufferedReader br,
			int length, Boolean loginChecked) throws IOException {
		if (requestType.equals("GET")) {
			getRequestType(dos, requestUrl, loginChecked);
		} else {
			postRequestType(dos, requestUrl, br, length);
		}
	}

	private void getRequestType(DataOutputStream dos, String requestUrl, Boolean loginChecked) throws IOException {
		if(requestUrl.equals("/user/list")) {
			loginMiddleWare(dos, loginChecked);
			StringBuilder userListStr = new StringBuilder();
			userListStr.append("Join User List");
			userListStr.append(newLine);
			userListStr.append(DataBase.findAll());
			userListStr.append(newLine);
			byte[] body = userListStr.toString().getBytes();
			response200Header(dos, body.length, requestUrl);
			responseBody(dos, body);
		}else {
			byte[] body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
			response200Header(dos, body.length, requestUrl);
			responseBody(dos, body);
		}
		
	}

	private void postRequestType(DataOutputStream dos, String requestUrl, BufferedReader br, int length)
			throws IOException {
		Map<String, String> map = HttpRequestUtils.parseQueryString(IOUtils.readData(br, (length)));
		if (requestUrl.equals("/user/create")) {
			User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
			DataBase.addUser(user);
			log.debug(user.toString());
			response302Header(dos, "index.html");
		} else if (requestUrl.equals("/user/login")) {
			try {
				User user = HttpRequestUtils.loginRequest(map.get("userId"), map.get("password"));
				log.debug("login success");
				responseLoginSucess302Header(dos, "index.html");
			} catch(Exception e) {
				log.debug("login failed");
				responseLoginFailed302Header(dos, "user/login_failed.html");
			}
		}
	}

	private void responseLoginSucess302Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /" + redirectUrl + "\r\n");
			dos.writeBytes("Set-Cookie: logined=true; Path=/" + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseLoginFailed302Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /" + redirectUrl + "\r\n");
			dos.writeBytes("Set-Cookie: logined=false; Path=/" + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String requestUrl) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			if(requestUrl.contains("html")) {
				dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			}else if(requestUrl.contains("css")) {
				dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			}
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response404Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /" + redirectUrl + " \r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
