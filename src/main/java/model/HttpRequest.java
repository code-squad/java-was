package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import model.response.HttpException;
import util.HttpRequestUtils;

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
		settingParameters(HttpRequestUtils.getParameterQuery(requestLine, headers, br));
		settingCookie(headers.get("Cookie"));
	}
	
	private void settingParameters(String query) {
		if (query != null) {
			parameters = HttpRequestUtils.parseQueryString(query);
		}
	}
	
	private void settingCookie(String cookieQuery) {
		if (cookieQuery != null) {
			cookie = new Cookie(cookieQuery);
		}
	}
	
	public String getHeader(String key) {
		return headers.get(key);
	}

	public String getParameter(String key) {
		return parameters.get(key);
	}

	public String getCookie(String key) {
		if (cookie != null) {
			return cookie.get(key);
		}
		throw new HttpException("쿠키가 없어서 문제가 발생하였다!");
	}

	public String getUrl() {
		return requestLine.getUrl();
	}

	public boolean isLogined() {
		if (cookie == null) {
			return false;
		}
		return "true".equals(cookie.get("logined"));
	}

}
