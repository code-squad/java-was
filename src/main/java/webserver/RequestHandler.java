package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

import java.nio.file.*;

import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestTypes;
import util.IOUtils;
import util.StringUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	private boolean login;
	
	
	
	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			DataOutputStream dos = new DataOutputStream(out);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String requestFirstLine = br.readLine();
			RequestTypes type = HttpRequestUtils.parseRequestType(requestFirstLine);
			String requestFile = StringUtils.directoryFromRequestHeader(requestFirstLine);
			int length = 0;
			
			if (type == RequestTypes.POST && requestFile.contains("/user/create")) {
				while(!requestFirstLine.equals("")) {
					requestFirstLine = br.readLine();
					if(requestFirstLine.contains("Content-Length")) {
						length = Integer.parseInt(requestFirstLine.split(" ")[1]);
					}
					
				}
				
				String postBody = IOUtils.readData(br, length);
				log.debug(postBody);
				Map<String, String> userInfo = HttpRequestUtils.parseQueryString(postBody);
				DataBase.addUser(User.createNewUser(userInfo));
				log.debug(DataBase.findUserById(userInfo.get("userId")).getName());
				response302Redirect(dos, "/index.html");
				
			}
			
			else if(type == RequestTypes.POST && requestFile.contains("/user/login")) {
				while(!requestFirstLine.equals("")) {
					requestFirstLine = br.readLine();
					if(requestFirstLine.contains("Content-Length")) {
						length = Integer.parseInt(requestFirstLine.split(" ")[1]);
					}
					
				}
				String postBody = IOUtils.readData(br, length);
				Map<String, String> loginInfo = HttpRequestUtils.parseQueryString(postBody);
				User user = DataBase.findUserById(loginInfo.get("userId"));
				if (user.isLoginInfoCorrect(loginInfo.get("password"))) {
					this.login = true;
					response302RedirectLoginCookie(dos, "/index.html");
				}
				else {
					this.login = false;
					response302Redirect(dos, "/user/login_failed.html");
				}
			}
			
			else if(requestFile.indexOf('?') >= 0) {
				log.debug("user info received.");
				String query = StringUtils.parseQueryString(requestFile);
				Map<String, String> UserInfo = HttpRequestUtils.parseQueryString(query);
				DataBase.addUser(new User(UserInfo.get("userId"), UserInfo.get("password"), UserInfo.get("name"), UserInfo.get("email")));
				log.debug(DataBase.findUserById(UserInfo.get("userId")).toString());
			}
			else {
			log.debug(requestFile);
			byte[] body = Files.readAllBytes(new File("./webapp" + requestFile).toPath());
			if (requestFile.contains("css")) {
				response200HeaderStaticCss(dos, body.length);
			} else {
				response200Header(dos, body.length);
			}
			responseBody(dos, body);
		}} catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			//dos.writeBytes(response200HeaderWithLoginCookie(this.login) + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private String response200HeaderWithLoginCookie(boolean login) {
		if(login) {
			return "Set-Cookie: logined=true; Path=/";
		}
		return "Set-Cookie: logined=false; Path=/";
	}

	private void response200HeaderStaticCss(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response302Redirect(DataOutputStream dos, String target) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + target);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response302RedirectLoginCookie(DataOutputStream dos, String target) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + target + "\r\n");
			dos.writeBytes("Set-Cookie: logined=true; Path=/");
			dos.writeBytes("\r\n");
		}
		catch (IOException e) {
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
