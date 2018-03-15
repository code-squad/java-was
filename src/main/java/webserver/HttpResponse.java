package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private OutputStream out;
	private Map<String, String> headers = new HashMap<>();

	public HttpResponse(OutputStream out) {
		this.out = out;
	}

	public void forward(String url) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		byte[] body = pathByteArray(url);
		dos.writeBytes("HTTP/1.1 200 OK \r\n");
		if (url.endsWith(".js")) {
			dos.writeBytes("Content-Type: application/javascript;charset=UTF-8\r\n");
		} else if (url.endsWith(".css")) {
			dos.writeBytes("Content-Type: text/css\r\n");
		} else {
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
		}
		dos.writeBytes("Content-Length: " + body.length + "\r\n");
		dos.writeBytes("\r\n");
		responseBody(dos, body);
	}

	public void sendRedirect(String url) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeBytes("HTTP/1.1 302 Found \r\n");
		dos.writeBytes("Location: " + url + "\r\n");
		setDynamicHeader(dos);
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

	private void setDynamicHeader(DataOutputStream dos) {
		Set<String> ks = headers.keySet();
		for (String key : ks) {
			try {
				dos.writeBytes(key + ":" + headers.get(key) + "\r\n");
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void setHeader(String key, String value) {
		headers.put(key, value);
	}

	public void forwardDynamic(byte[] body) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeBytes("HTTP/1.1 200 OK \r\n");
		dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
		dos.writeBytes("Content-Length: " + body.length + "\r\n");
		dos.writeBytes("\r\n");
		responseBody(dos, body);
	}
}
