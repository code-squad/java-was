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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			DataOutputStream dos = new DataOutputStream(out);
			String line = br.readLine();
			String method = HttpRequestUtils.parseMethod(line);
			if("POST".equals(method)) {
				handlePost(br, dos, line);
			}
			if("GET".equals(method)) {
				handleGet(br, dos, line);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void handleGet(BufferedReader br, DataOutputStream dos, String line) throws IOException {
		String url = HttpRequestUtils.parseUrl(line);
		Map<String, String> headers = HttpRequestUtils.pasrseHeaders(line, br);
		if("/user/list".equals(url)) {
			Map<String, String> cookies = HttpRequestUtils.parseCookies( headers.get("Cookie"));
			String isLogined = cookies.get("logined");
			if ( isLogined == null || isLogined.equals("false") ) {
				response302Header(dos, "/user/login.html");	
			} else {
				byte[] body = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
				response200(dos, IOUtils.addUserList(body), headers);
			}
		} else {
			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
			response200(dos, body, headers);
		}
	}

	private void handlePost(BufferedReader br, DataOutputStream dos, String line) throws IOException {
		String url = HttpRequestUtils.parseUrl(line);
		Map<String, String> headers = HttpRequestUtils.pasrseHeaders(line, br);
		String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(query);
		if("/user/login".equals(url)) {
			User user = DataBase.findUserById(parameters.get("userId"));
			if(user != null && user.matchPassword(parameters.get("password"))) {
				response302CookieHeader(dos, "/index.html", "true");
			} else {
				response302CookieHeader(dos, "/user/login_failed.html", "false");
			}
		}
		if("/user/create".equals(url)) {
			DataBase.addUser(new User(parameters.get("userId"),parameters.get("password"),parameters.get("name"),parameters.get("email")));
			response302Header(dos, "/index.html");
		}
	}
	
	private void response200(DataOutputStream dos, byte[] body, Map<String, String> headers) {
		response200Header(dos, body.length, headers);
		responseBody(dos, body);
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, Map<String, String> headers) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: " + headers.get("Accept") + "\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response302CookieHeader(DataOutputStream dos, String url, String isLogined) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Set-Cookie: logined=" + isLogined + "; Path=/ \r\n");
			dos.writeBytes("Location: " + url);
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + url);
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
