package model.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	private Map<String, String> headers;
	private boolean hasBody;
	private byte[] body;

	protected HttpResponse(String status) {
		headers = new HashMap<>();
		headers.put("status", status);
		hasBody = false;
	}

	public abstract void setUrl(String url);
	
	public boolean hasBody() {
		return hasBody;
	}
	
	public void putBody(byte[] body) {
		this.body = body;
		this.hasBody = true;
		headers.put("Content-Length", Integer.toString(body.length));
	}
	
	public void setContentType(String type) {
		headers.put("Content-Type", type);
	}

	public void writeResponse(DataOutputStream dos) {
		try {
			dos.writeBytes(headers.remove("status"));
			for (String key : headers.keySet()) {
				dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
			}
			dos.writeBytes("\r\n");
			if( hasBody() ) {
				dos.write(body);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	protected void setHeaders(String key, String value) {
		headers.put(key, value);
	}
	
	public void setCookieLogined(boolean isLogined) {
		headers.put("Set-Cookie", "logined=" + isLogined + "; Path=/");
	}
}
