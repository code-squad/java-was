package http.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private static final String NEWLINE = "\r\n";
	private Map<String, String> headers;
	private boolean hasBody;
	private byte[] body;
	private DataOutputStream dos;

	public HttpResponse(OutputStream out) {
		headers = new HashMap<>();
		this.dos = new DataOutputStream(out);
	}
	
	public void sendRedirect(String path) {
		headers.put("requestFirstLine", "HTTP/1.1 302 Found \r\n");
		addHeader("Location", path);
		writeResponse();
	}
	
	public void forward(String path) {
		headers.put("requestFirstLine", "HTTP/1.1 200 OK \r\n");
		try {
			this.body = Files.readAllBytes(new File("./webapp" + path).toPath());
			this.hasBody = true;
			headers.put("Content-Length", Integer.toString(body.length));
		} catch (IOException e) {
			log.error("body를 읽어오면서 에러가 났다!!");
		}
		writeResponse();
	}
	
	public boolean hasBody() {
		return hasBody;
	}
	
	public void setContentType(String type) {
		headers.put("Content-Type", type);
	}
	
	private void writeResponse() {
		try {
			dos.writeBytes(headers.remove("requestFirstLine"));
			for (String key : headers.keySet()) {
				dos.writeBytes(key + ": " + headers.get(key) + NEWLINE);
			}
			dos.writeBytes(NEWLINE);
			if( hasBody() ) {
				dos.write(body);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	protected void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	public void setCookieLogined(boolean isLogined) {
		headers.put("Set-Cookie", "logined=" + isLogined + "; Path=/");
	}
}
