package http.request;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Method;
import util.HttpRequestUtils;

public class RequestLine {
	private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
	private Method method;
	private String path;
	private String query;
	private Map<String, String> parameter;

	public RequestLine(String line) {
		log.info(line);
		method = Method.valueOf(HttpRequestUtils.parseMethod(line));
		adjustUrl(HttpRequestUtils.parseUrl(line));
		createParameter();
	}

	private void createParameter() {
		Optional<Map<String, String>> optional = Optional.ofNullable(query).map(HttpRequestUtils::parseQueryString);
		if (optional.isPresent()) {
			parameter = optional.get();
		}
	}

	private void adjustUrl(String url) {
		String[] urlArr = url.split("\\?");
		path = urlArr[0];
		if (urlArr.length > 1) {
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

	public String getParameter(String key) {
		return parameter.get(key);
	}

	public boolean hasParameter() {
		return parameter != null;
	}
}
