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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
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
			HttpRequest httpRequest = HttpRequest.of(in);
			String url = httpRequest.getUrl();

			if (url.startsWith("/create")) {
//				int index = url.indexOf("?");
//				String requestPath = url.substring(0, index);
//				String queryString = url.substring(index + 1);
//				params=
//				HttpRequestUtils.parseQueryString(queryString);
				
//				Map<String, String> params = httpRequest.requestBody();
				User user = new User(httpRequest.getParameter("userId"), httpRequest.getParameter("password"), httpRequest.getParameter("name"),
						httpRequest.getParameter("email"));
				log.debug("User : {}", user.toString());
				DataBase.addUser(user);

				DataOutputStream dos = new DataOutputStream(out);
				response302Header(dos);
			} else if (url.equals("/user/login")) {
//				Map<String, String> params = httpRequest.requestBody();
				log.debug("userId : {} , password :{}", httpRequest.getParameter("userId"), httpRequest.getParameter("password"));
				User user = DataBase.findUserById(httpRequest.getParameter("userId"));
				if (user == null) {
					log.debug("User Not Found!");
					DataOutputStream dos = new DataOutputStream(out);
					response302HeaderWithCookie(dos, "logined=fail", "/user/login_failed.html");
				} else if (user.getPassword().equals(httpRequest.getParameter("password"))) {
					log.debug("login success!!");
					DataOutputStream dos = new DataOutputStream(out);
					response302HeaderWithCookie(dos, "logined=true", "/index.html");
				} else {
					log.debug("password mismatch!!");
					DataOutputStream dos = new DataOutputStream(out);
					response302HeaderWithCookie(dos, "logined=fail", "/user/login_failed.html");
				}
			} else if (url.startsWith("/user/list")) {
				log.debug("headers: Set-Cookie : {}", httpRequest.getHeader("Cookie"));
				if (httpRequest.getHeader("Cookie").contains("true")) {
					log.debug("login ok {}" + url);
					DataOutputStream dos = new DataOutputStream(out);
					byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

					StringBuilder sb = new StringBuilder(body.toString());
					log.debug("ss {}", sb);

					response200Header(dos, body.length);
					responseBody(dos, body);
				} else {
					log.debug("no login");
					DataOutputStream dos = new DataOutputStream(out);
					response302HeaderWithCookie(dos, "logined=fail", "/user/login.html");
				}
			} else if (url.endsWith(".css")) {
				DataOutputStream dos = new DataOutputStream(out);
				byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
				response200HeaderWithCss(dos, body.length);
				responseBody(dos, body);
			} else {
				DataOutputStream dos = new DataOutputStream(out);
				byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
				response200Header(dos, body.length);
				responseBody(dos, body);
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200HeaderWithCss(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + url + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302HeaderWithCookie(DataOutputStream dos, String cookie, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + url + "\r\n");
			dos.writeBytes("Set-Cookie: " + cookie + " Path=/\r\n");
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
