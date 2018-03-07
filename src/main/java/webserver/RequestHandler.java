package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;
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
			byte[] body = pathByteArray("/index.html");

			if (url.equals("/index.html")) {
				log.debug("if statement - /index.html");
				response200(pathByteArray(url), out);
			}

			if (url.equals("/user/form.html")) {
				log.debug("if statement - /user/form.html");
				response200(pathByteArray(url), out);
			}

			if (url.equals("/user/create")) {
				log.debug("if statement - /user/create");
				if (method.equals("GET")) {
					createUser(param);
				}
				if (method.equals("POST")) {
					createUser(param);
				}
				response302(out);
			}
			response200(body, out);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200(byte[] body, OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);
		response200Header(dos, body.length);
		responseBody(dos, body);
	}

	private void response302(OutputStream out) {
		DataOutputStream dos = new DataOutputStream(out);
		response302Header(dos);
	}

	void createUser(Map<String, String> param) throws UnsupportedEncodingException {
		DataBase.addUser(new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email")));
		log.debug("[ createUser method ]  - user name : "
				+ URLDecoder.decode(DataBase.findUserById("javajigi").getName(), "UTF-8"));
	}

	byte[] pathByteArray(String url) throws IOException {
		return Files.readAllBytes(new File("./webapp" + url).toPath());
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

	private void response302Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");
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
