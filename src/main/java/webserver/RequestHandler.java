package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import util.StringUtils;

public class RequestHandler extends Thread {
	StringUtils su = new StringUtils();
	HttpRequestUtils hru = new HttpRequestUtils();
	IOUtils io = new IOUtils();
	DataBase db = new DataBase();

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
			String line = br.readLine();
			String[] tokens = line.split(" ");
			String url = su.generatePath(tokens);
			Map<String, String> headers = extractHeaders(br, line);
			String postValue = extractPostValue(br, url, headers);
			
			DataOutputStream dos = new DataOutputStream(out);
			byte[] body = generateOutputStream(out, url, postValue, dos);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private Map<String, String> extractHeaders(BufferedReader br, String line) throws IOException {
		String[] tokens;
		Map<String, String> headers = new HashMap <String,String>();
		while(!"".equals(line)) {
			line = br.readLine();
			tokens = line.split(": ");
			if(tokens.length ==2) {
				headers.put(tokens[0], tokens[1]);
			}
		}
		return headers;
	}

	private String extractPostValue(BufferedReader br, String url, Map<String, String> headers)
			throws IOException {
		String postValue = new String();
		if (su.checkCreateUser(url)) {
			postValue = io.readData(br, Integer.parseInt(headers.get("Content-Length")));
			User user = su.generateUser(postValue);
			db.addUser(user);
		} else if (su.checkLogin(url)) {
			postValue = io.readData(br, Integer.parseInt(headers.get("Content-Length")));
		}
		return postValue;
	}

	private byte[] generateOutputStream(OutputStream out, String url, String postValue, DataOutputStream dos)
			throws IOException {
		byte[] body;
		if (su.checkCreateUser(url)) {
			body = Files.readAllBytes(new File("./webapp/index.html").toPath());
			response302Header(dos);
			return body;
		}
		if (su.checkLogin(url)) {
			return generateLoginHeader(postValue, dos);
		}
		body = Files.readAllBytes(new File("./webapp" + url).toPath());
		response200Header(dos, body.length);
		return body;

	}

	private byte[] generateLoginHeader(String postValue, DataOutputStream dos) throws IOException {
		byte[] body;
		User user = su.loginUser(postValue);
		if (su.confirmUser(user, db)) {
			body = Files.readAllBytes(new File("./webapp/index.html").toPath());
			response200LoginSuccessHeader(dos);
			return body;
		}
		body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
		response200LoginFailHeader(dos, body.length);
		return body;
	}


	private void response200LoginFailHeader(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css,*/*;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("Set-Cookie: logined=false; Path=/\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200LoginSuccessHeader(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");
			dos.writeBytes("Set-Cookie: logined=true; Path=/\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css,*/*;charset=utf-8\r\n");
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
