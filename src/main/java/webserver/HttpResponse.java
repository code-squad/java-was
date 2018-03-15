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

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private OutputStream out;

	public HttpResponse(OutputStream out) {
		this.out = out;
	}

	public void forward(String url) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		byte[] body = pathByteArray("/index.html");

		if (!url.equals("/") & url.contains("/")) {
			body = pathByteArray(url);
		}

		if (url.equals("loginList")) {
			body = setUserListOut();
		}

		dos.writeBytes("HTTP/1.1 200 OK \r\n");

		if (url.endsWith(".js")) {
			dos.writeBytes("Content-Type: application/javascript;charset=UTF-8\r\n");
		}

		else if (url.endsWith(".css")) {
			dos.writeBytes("Content-Type: text/css\r\n");
		}

		else {
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
		}

		dos.writeBytes("Content-Length: " + body.length + "\r\n");
		dos.writeBytes("\r\n");
		responseBody(dos, body);
	}

	public void sendRedirect(String url, boolean login) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeBytes("HTTP/1.1 302 Found \r\n");
		dos.writeBytes("Location: " + url + "\r\n");
		if (login) {
			dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
		}
		if (!login) {
			dos.writeBytes("Set-Cookie: logined=false; Path=/ \r\n");
		}
		dos.writeBytes("\r\n");
	}

	byte[] pathByteArray(String url) throws IOException {
		return Files.readAllBytes(new File("./webapp" + url).toPath());
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private byte[] setUserListOut() {
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
		return sb.toString().getBytes();
	}
}
