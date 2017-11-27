package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

import http.Cookie;
import http.Method;
import http.response.HttpResponseException;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	private Map<String, String> headers;
	private Map<String, String> parameters;
	private RequestLine requestLine;
	private Cookie cookie;

	public HttpRequest(InputStream in) throws IOException {
		analysisRequest(in);
	}

	public void analysisRequest(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		requestLine = new RequestLine(br.readLine());
		headers = HttpRequestUtils.parseHeaders(br);
		if (headers.get("Content-Length") != null) {
			String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
			Optional<Map<String, String>> optional = Optional.ofNullable(query).map(HttpRequestUtils::parseQueryString);
			parameters = optional.get();
		}
		settingCookie(headers.get("Cookie"));
	}

	private void settingCookie(String cookieQuery) {
		if (cookieQuery != null) {
			cookie = new Cookie(cookieQuery);
		}
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public Method getMethod() {
		return requestLine.getMethod();
	}

	public String getParameter(String key) {
		if (requestLine.hasParameter()) {
			return requestLine.getParameter(key);
		}
		return parameters.get(key);
	}

	public String getCookie(String key) {
		if (cookie != null) {
			return cookie.get(key);
		}
		throw new HttpResponseException("쿠키가 없어서 문제가 발생하였다!");
	}

	public String getPath() {
		return requestLine.getPath();
	}

	public boolean isLogined() {
		if (cookie == null) {
			return false;
		}
		return "true".equals(cookie.get("logined"));
	}

}
