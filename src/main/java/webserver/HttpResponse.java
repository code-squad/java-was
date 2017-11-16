package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import mvc.Controller;
import util.HttpRequestUtils;
import util.StringUtils;
import webserver.RequestHandler;

public class HttpResponse {
	byte[]body;
	DataBase db;
	String url;
	String postValue;
	StringUtils su = new StringUtils();
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	DataOutputStream dos;
	OutputStream out;
	
	HttpResponse(HttpRequest request, OutputStream out) {
		this.out  = out;
		this.dos = new DataOutputStream(out);
		this.db = request.getDb();
		this.url = request.getUrl();
		this.postValue = request.getPostValue();
	}

	public void generateBaseHeader() throws IOException {
		body = Files.readAllBytes(new File("./webapp" + url).toPath());
		response200Header(body.length);
	}

	public void generateListUserHeader() {
		body = makeUserList();
		response200Header(body.length);
	}

	public void generateCreateUserHeader() throws IOException {
		body = Files.readAllBytes(new File("./webapp/index.html").toPath());
		response302Header();
	}

	private byte[] makeUserList() {
		Collection<User> users = db.findAll();
		StringBuilder sb = new StringBuilder();
		for (User user : users) {
			sb.append("userId = " + user.getUserId() + " name = " + user.getName());
		}
		return sb.toString().getBytes();
	}

	public void generateLoginHeader() throws IOException {
		User user = loginUser(postValue);
		if (confirmUser(user, db)) {
			 generateLoginSuccessHeader();
		}
		 generateLoginFailHeader();
	}

	private void generateLoginFailHeader() throws IOException {
		
		body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
		response200LoginFailHeader(body.length);
	}

	private void generateLoginSuccessHeader() throws IOException {
		
		body = Files.readAllBytes(new File("./webapp/index.html").toPath());
		response200LoginSuccessHeader();
	}

	public boolean confirmUser(User user, DataBase db) {

		if (db.findUserById(user.getUserId()) == null) {
			return false;
		}
		if (!db.findUserById(user.getUserId()).getPassword().equals(user.getPassword())) {
			return false;
		}
		return true;
	}

	private void response200LoginFailHeader(int lengthOfBodyContent) {
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

	public User loginUser(String url) {
		HttpRequestUtils hru = new HttpRequestUtils();
		String userId = hru.parseQueryString(url).get("userId");
		String password = hru.parseQueryString(url).get("password");
		return new User(userId, password);
	}

	private void response200LoginSuccessHeader() {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");
			dos.writeBytes("Set-Cookie: logined=true; Path=/\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css,*/*;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header() {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /index.html\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void responseBody() {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}


	public String getUrl() {
		return url;
	}

}
