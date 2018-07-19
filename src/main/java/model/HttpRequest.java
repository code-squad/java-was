package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	private static final String POST = "POST";

	private HttpHeaders headers;
	private Parameter parameter;
	private String url;
	private String method;

	public HttpRequest() {
	}


	public HttpRequest(HttpHeaders headers, String url, String method, Parameter parameter) {
		this.headers = headers;
		this.url = url;
		this.method = method;
		this.parameter = parameter;
	}

	public static HttpRequest of(InputStream in) throws IOException {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String firstLine = bufferReader.readLine();
		log.debug("firstLine: {}", firstLine);

		HttpHeaders headers = makeHeaders(bufferReader, firstLine);
		
		if (HttpRequestUtils.getMethod(firstLine).equals(POST)) {
			return new HttpRequest(headers, HttpRequestUtils.getUrl(firstLine),	HttpRequestUtils.getMethod(firstLine),  Parameter.of(bufferReader, headers.findByHeaderName("Content-Length")));
		}
		return new HttpRequest(headers, HttpRequestUtils.getUrl(firstLine),	HttpRequestUtils.getMethod(firstLine),  Parameter.of(HttpRequestUtils.getUrl(firstLine)));

	}

	public static HttpHeaders makeHeaders(BufferedReader bufferReader, String line) throws IOException {
		Map<String, String> headers = new HashMap<String, String>();
		while (!"".equals(line)) {
			line = bufferReader.readLine();
			log.debug("header : {}", line);
			List<String> headerTokens = Arrays.asList(line.split(": "));
			if (headerTokens.size() == 2) {
				headers.put(headerTokens.get(0), headerTokens.get(1));
			}
		}
		return new HttpHeaders(headers);
	}


	public String getParameter(String name) {
		return parameter.getParameter(name);
	}

	public String getHeader(String headerName) {
		return headers.findByHeaderName(headerName);
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public String getUrl() {
		return url;
	}
	
	public Boolean urlCorrect(String mappingUrl) {
		return url.equals(mappingUrl);
	}

	public String getMethod() {
		return method;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((headers == null) ? 0 : headers.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpRequest other = (HttpRequest) obj;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HttpRequest [headers=" + headers + ", url=" + url + ", method=" + method + "]";
	}

}
