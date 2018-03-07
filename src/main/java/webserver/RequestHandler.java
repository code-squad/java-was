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
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			byte[] body = readInput(in);
			DataOutputStream dos = new DataOutputStream(out);
			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	byte[] readInput(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String line = br.readLine();
		String[] splitLine = HttpRequestUtils.splitString(line);
		String urlWithParam = splitLine[1];
		while (!"".equals(line)) {
			if (line == null) {
				break;
			}
			line = br.readLine();
			log.debug("header : " + line);
		}
		String[] urlArray = HttpRequestUtils.splitUrl(urlWithParam);
		String url = urlArray[0];
		if (urlArray.length >= 2) {
			createUser(urlArray[1]);
		}

		byte[] body = pathByteArray(url);
		if (url.equals("/index.html")) {
			log.debug("if statement - /index.html");
			return pathByteArray(url);
		}

		if (url.equals("/user/form.html")) {
			log.debug("if statement - /user/form.html");
			return pathByteArray(url);
		}
		return body;
	}
	
	
	
	
	
	
	
	
	void createUser(String queryString) throws UnsupportedEncodingException {
		Map<String, String> userMap = HttpRequestUtils.parseQueryString(queryString);
		DataBase.addUser(
				new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email")));
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

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
