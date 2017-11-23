package http;

import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;

public class Cookie {
	private Map<String, String> cookie;

	public Cookie(String query) {
		if( query != null ) {
			cookie = HttpRequestUtils.parseCookies(query);
		} else {
			cookie = new HashMap<>();
		}
	}
	
	public String get(String key) {
		return cookie.get(key);
	}
}
