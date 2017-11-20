package model.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpResponse {
	private Map<String, String> headers;
	private boolean hasBody;
	private byte[] body;

	public HttpResponse() {
		headers = new HashMap<>();
		hasBody = false;
	}

	public abstract String createHeader();

	public void responseUrl(String url) throws IOException {
		this.body = Files.readAllBytes(new File("./webapp" + url).toPath());
		this.hasBody = true;
		setHeader("Content-Length", Integer.toString(body.length));
	}
	
	public void responseType(String type) {
		setHeader("Content-Type",type);
	}

	public byte[] getBody() {
		if (hasBody) {
			return body;
		}
		throw new HttpResponseException("no body no body");
	}

	public boolean hasBody() {
		return hasBody;
	}

	protected void setHeader(String key, String value) {
		headers.put(key, value);
	}

	protected String getHeader(String key) {
		return headers.get(key);
	}
}
