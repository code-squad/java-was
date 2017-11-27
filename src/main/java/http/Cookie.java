package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;

public class Cookie {
	private Map<String, String> cookie;

	public Cookie() {
		cookie = new HashMap<>();
	}
	
	public Cookie(String query) {
		if (query != null) {
			cookie = HttpRequestUtils.parseCookies(query);
		} else {
			cookie = new HashMap<>();
		}
	}

	public String get(String key) {
		return cookie.get(key);
	}

	public void put(String key, String value) {
		cookie.put(key, value);
	}

	public void writeResponse(DataOutputStream dos) throws IOException {
		dos.writeBytes("Set-Cookie: ");
		for (String key : cookie.keySet()) {
			dos.writeBytes( key + "=" + cookie.get(key) + ";");
		}
		dos.writeBytes(" Path=/\r\n");
	}

	public boolean isEmpty() {
		return cookie.isEmpty();
	}
}
