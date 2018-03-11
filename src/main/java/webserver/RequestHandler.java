package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	private Boolean login = false;

	public RequestHandler() {
	}

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();) {
			HttpRequest reqst = new HttpRequest(in);
			String url = reqst.getURI();
			String method = reqst.getMethod();
			Map<String, String> param = reqst.getParam();
			String logined = reqst.getLogined();
			log.debug("-----------------------logined : " + logined);
			byte[] body = pathByteArray("/index.html");

			if (url.equals("/")) {
				log.debug("if statement - /");
				response200(body, out);
			}

			if (url.equals("/index.html")) {
				log.debug("if statement - /index.html");
				response200(pathByteArray(url), out);
			}

			if (url.equals("/user/form.html")) {
				log.debug("if statement - /user/form.html");
				response200(pathByteArray(url), out);
			}

			if (url.equals("/user/login.html")) {
				log.debug("if statement - /user/login.html");
				response200(pathByteArray(url), out);
			}

			if (url.equals("/user/login_failed.html")) {
				log.debug("if statement - /user/login_failed.html");
				response200(pathByteArray(url), out);
			}

			if (url.equals("/css/styles.css")) {
				log.debug("if statement - /css/styles.css");
				response200CSS(pathByteArray(url), out);
			}
			
			if (url.equals("/user/create")) {
				log.debug("if statement - /user/create");
				if (method.equals("GET")) {
					createUser(param);
				}
				if (method.equals("POST")) {
					createUser(param);
				}
				response302Header("/index.html", out);
			}

			if (url.equals("/user/login")) {
				log.debug("if statement - /user/login");
				login = loginCheck(param);
				if (login) {
					response302Header("/index.html", out);
				}
				response302Header("/user/login_failed.html", out);
			}
			
			if (url.equals("/user/list")) {
				log.debug("if statement - /user/list");
				if (logined.contains("logined=true;")) {
					Collection<User> users = DataBase.findAll();
					StringBuilder sb = new StringBuilder();
					sb.append("<table>");
					for (User user : users) {
						sb.append("<tr>");
						sb.append("<td>" + user.getUserId() + "</td>");
						sb.append("<td>" + user.getName() + "</td>");
						sb.append("<td>" + user.getEmail() + "</td>");
						sb.append("</tr>");
					}
					sb.append("</table>");
					body = sb.toString().getBytes();
					response200(body, out);
				}
				if (logined.contains("logined=false;")) {
					response302Header("/user/login.html", out);
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	void createUser(Map<String, String> param) throws UnsupportedEncodingException {
		DataBase.addUser(new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email")));
		log.debug("[ createUser method ] SIZE : " + DataBase.findAll().size());
	}

	boolean loginCheck(Map<String, String> param) {
		return DataBase.findUserById(param.get("userId")).getPassword().equals(param.get("password"));
	}

	byte[] pathByteArray(String url) throws IOException {
		return Files.readAllBytes(new File("./webapp" + url).toPath());
	}

	private void response200(byte[] body, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);
		response200Header(dos, body.length);
		responseBody(dos, body);
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response200CSS(byte[] body, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);
		response200CSSHeader(dos, body.length);
		responseBody(dos, body);
	}
	
	private void response200CSSHeader(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css \r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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

	private void response302Header(String url, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + url + "\r\n");
			if (login) {
				dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
			}
			if (!login) {
				dos.writeBytes("Set-Cookie: logined=false; Path=/ \r\n");
			}
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
