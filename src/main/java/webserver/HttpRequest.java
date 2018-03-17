package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	private Map<String, String> headers = new HashMap<>();
	private Map<String, String> params = new HashMap<>();
	private RequestLine requestLine;
	
	public HttpRequest(InputStream in) throws IOException {
		readLine(new BufferedReader(new InputStreamReader(in, "UTF-8")));
	}

	private void readLine(BufferedReader br) throws IOException {
		String line = br.readLine();
		log.debug("FIRST LINE : " + line);
		setMethodAndPath(line);
		line = repeatReadLine(br, line);
		HttpMethod method = getMethod();
		String uri = requestLine.getUri();

		if (method.isGet() & requestLine.isContains("?")) {
			extractGetParam(uri);
		}

		if (method.isPost() & headers.get(HttpHeader.CONTENTLENGTH.getHeader()) != null) {
			createParam(IOUtils.readData(br, Integer.parseInt(headers.get(HttpHeader.CONTENTLENGTH.getHeader()))));
		}
	}
	
	private void setMethodAndPath(String line) {
		String[] splitLine = HttpRequestUtils.splitStringBlank(line);
		requestLine = new RequestLine(splitLine);
	}

	private String repeatReadLine(BufferedReader br, String line) throws IOException {
		while (!"".equals(line)) {
			line = br.readLine();
			log.debug("header : " + line);
			if (line == null) {
				break;
			}
			headerToMap(line);
		}
		return line;
	}

	private void headerToMap(String line) {
		String[] splitLine = HttpRequestUtils.splitString(line);
		if (!"".equals(splitLine[0])) {
			headers.put(splitLine[0], splitLine[1]);
		}
	}
		
	private void createParam(String line) {
		params = HttpRequestUtils.parseQueryString(line);
	}

	private void extractGetParam(String line) {
		String[] splitGet = HttpRequestUtils.splitUrl(line);
		createParam(requestLine.splitUriParam(splitGet));
	}

	public HttpMethod getMethod() {
		return HttpMethod.getEnum(requestLine.getMethod());
	}

	public String getURI() {
		return requestLine.getUri();
	}

	public Map<String, String> getParam() {
		return params;
	}

	public String getLogined() {
		if (headers.get(HttpHeader.LOGINED.getHeader()) == null) {
			return "logined=false;";
		}
		return headers.get(HttpHeader.LOGINED.getHeader());
	}

	public String getAccept() {
		return headers.get(HttpHeader.ACCEPT.getHeader());
	}
	
	public String getCookie() {
		return headers.get(HttpHeader.COOKIE.getHeader());
	}
}
