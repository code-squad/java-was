package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
	private String requestType;
	private String url;
	private String cookie;
	private Boolean login;
	private int contentLength;
	private String body;
	
	public HttpRequestParser(InputStream in) throws IOException {
		this.login = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		this.requestType = HttpRequestUtils.getFirstHeader(line, 0);
		this.url = HttpRequestUtils.getFirstHeader(line, 1);
		while (!line.equals("")) {
			line = br.readLine();
			searchHeaderResource(line);
		}
		this.body = getBodyData(br, this.contentLength);
	}
	
	public static String getBodyData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
	
	public void getCookieHeader(String line) {
		if(line.contains("Cookie")){
			this.cookie = line.split(" ")[1];
			this.login = Boolean.valueOf(this.cookie.split("=")[1]).booleanValue();;
		}
	}
	
	public void getContentLengthHeader(String line) {
		if(line.contains("Content-Length")){
			this.contentLength = Integer.parseInt(line.split(" ")[1]);
		}
	}
	
	public void searchHeaderResource(String line) {
		getCookieHeader(line);
		getContentLengthHeader(line);
	}
	public String getRequestType() {
		return requestType;
	}
	public String getUrl() {
		return url;
	}
	public String getCookie() {
		return cookie;
	}
	public Boolean getLogin() {
		return login;
	}
	public int getContentLength() {
		return contentLength;
	}
	public String getBody() {
		return body;
	}
	
	
	
	
}
