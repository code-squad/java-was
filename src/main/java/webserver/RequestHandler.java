package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.Method;
import model.User;

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
			DataOutputStream dos = new DataOutputStream(out);
			HttpRequest request = new HttpRequest(in);
			
			if(Method.Post.equals(request.getHeader("method"))) {
				handlePost(request, dos);
			}
			if(Method.Get.equals(request.getHeader("method"))) {
				handleGet(request, dos);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void handleGet(HttpRequest request, DataOutputStream dos) throws IOException {
		String url = request.getHeader("url");
		if("/user/list".equals(url)) {
			String isLogined = request.getCookie("logined");
			
			if ( isLogined == null || isLogined.equals("false") ) {
				response302Header(dos, "/user/login.html");	
			} else {
				byte[] body = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
				response200(dos, DataBase.addUserList(body), request);
			}
		} else {
			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
			response200(dos, body, request);
		}
	}

	private void handlePost(HttpRequest request, DataOutputStream dos) {
		String url = request.getUrl();
		if("/user/login".equals(url)) {
			User user = DataBase.findUserById(request.getParameter("userId"));
			if(user != null && user.matchPassword(request.getParameter("password"))) {
				response302CookieHeader(dos, "/index.html", "true");
			} else {
				response302CookieHeader(dos, "/user/login_failed.html", "false");
			}
		}
		if("/user/create".equals(url)) {
			DataBase.addUser(new User(request.getParameter("userId"),request.getParameter("password"),request.getParameter("name"),request.getParameter("email")));
			response302Header(dos, "/index.html");
		}
	}

	private void response200(DataOutputStream dos, byte[] body, HttpRequest request) {
		response200Header(dos, body.length, request);
		responseBody(dos, body);
	}
	
	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, HttpRequest request) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: " + request.getHeader("Accept") + "\r\n");
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
