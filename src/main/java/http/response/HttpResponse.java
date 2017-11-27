package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Cookie;
import http.api.readbodycontroller.ReadBodyController;
import http.api.readbodycontroller.ReadBodyControllerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private static final String NEWLINE = "\r\n";
	private Map<String, String> headers;
	private byte[] body;
	private DataOutputStream dos;
	private Cookie cookie;

	public HttpResponse(OutputStream out) {
		this.headers = new HashMap<>();
		this.dos = new DataOutputStream(out);
		this.cookie = new Cookie();
	}
	
	public void sendRedirect(String path) {
		headers.put("requestFirstLine", "HTTP/1.1 302 Found \r\n");
		addHeader("Location", path);
		sendResponse();
	}
	
	public void forward(String path) {
		headers.put("requestFirstLine", "HTTP/1.1 200 OK \r\n");
		updateBody(path);
		sendResponse();
	}

	private void updateBody(String path) {
		try {
			ReadBodyController nb = ReadBodyControllerFactory.find(path);
			this.body = nb.readBody(path);
		} catch (IOException e) {
			log.error("body를 읽어오면서 에러가 났다!!");
		}
		headers.put("Content-Length", Integer.toString(body.length));
	}
	
	public boolean hasBody() {
		return body != null;
	}
	
	public void setContentType(String type) {
		headers.put("Content-Type", type);
	}
	
	private void sendResponse() {
		try {
			dos.writeBytes(headers.remove("requestFirstLine"));
			for (String key : headers.keySet()) {
				dos.writeBytes(key + ": " + headers.get(key) + NEWLINE);
			}
			if( !cookie.isEmpty() ) {
				cookie.writeResponse(dos);
			}
			dos.writeBytes(NEWLINE);
			if( hasBody() ) {
				dos.write(body);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	public void setCookie(String key, String value) {
		cookie.put(key, value);
	}
}
