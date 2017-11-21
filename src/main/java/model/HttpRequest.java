package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import model.response.HttpException;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	private Map<String, String> headers;
	private Map<String, String> parameters;
	private Cookie cookie;

	public HttpRequest(InputStream in) throws IOException {
		analysisRequest(in);
	}

	public void analysisRequest(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		headers = HttpRequestUtils.pasrseHeaders(br);
		String method = headers.get("method");

		if (Method.POST.equals(method)) {
			String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
			parameters = HttpRequestUtils.parseQueryString(query);
		}

		if (Method.GET.equals(method)) {
			String query = HttpRequestUtils.parseQueryByUrl(headers.get("url"));
			if (query != null) {
				parameters = HttpRequestUtils.parseQueryString(query);
			}
		}
		if( headers.get("Cookie") != null ) {
			cookie = new Cookie(headers.get("Cookie"));
		}
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public String getParameter(String key) {
		return parameters.get(key);
	}
	
	public String getCookie(String key) {
		if( cookie != null ) {
			return cookie.get(key);
		}
		throw new HttpException("쿠키가 없어서 문제가 발생하였다!");
	}

	public String getUrl() {
		return headers.get("url");
	}

	public boolean isLogined() {
		if( cookie == null ) {
			return false;
		}
		return "true".equals(cookie.get("logined"));
	}

}
