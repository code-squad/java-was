package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Method;
import util.HttpRequestUtils;

public class RequestLine {
	private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
	private Method method;
	private String path;
	private String query;
	
	public RequestLine(String line){
		log.info(line);
		method = Method.valueOf(HttpRequestUtils.parseMethod(line));
		adjustUrl(HttpRequestUtils.parseUrl(line));
	}
	
	private void adjustUrl(String url) {
		String[] urlArr = url.split("\\?");
		path = urlArr[0];
		if( urlArr.length > 1) {
			query = urlArr[1];
		}
	}

	public Method getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public boolean matchMethod(Method method) {
		return this.method == method;
	}
}
