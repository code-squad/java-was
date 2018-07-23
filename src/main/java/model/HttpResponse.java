package model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private DataOutputStream dos = null;
	private Map<String, String> headers = new HashMap<String, String>();

	
	public HttpResponse() {
	}
	
	public HttpResponse(OutputStream out) {
		dos = new DataOutputStream(out);
	}

	public void forward(String url) throws IOException {

		try {
			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

			if (url.endsWith(".css")) {
				headers.put("Content-Type", "text/css");
			} else {
				headers.put("Content-Type", "text/html;charset=utf-8");
			}
			headers.put("Content-Length", body.length + "");
			response200Header();
			responseBody(body);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	public void forwardBody(String body) {
		byte[] contents = body.getBytes();
		headers.put("Content-Type", "text/html;charset=utf-8");
		headers.put("Content-Length", contents.length + "");
		response200Header();
		responseBody(contents);
	}

	public void addHeader(String name, String value) throws IOException {
		headers.put(name, value);
	}

	private void response200Header() {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			processHeaders();
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void sendRedirect(String location) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			processHeaders();
			dos.writeBytes("Location: " + location + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void processHeaders() {
		try {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				dos.writeBytes(key + ": " + headers.get(key) + " \r\n");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
