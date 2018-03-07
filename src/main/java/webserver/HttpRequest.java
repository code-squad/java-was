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

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
	private Map<Object, Object> httpMap = new HashMap<>();

	public HttpRequest(InputStream in) throws IOException {
		readLine(new BufferedReader(new InputStreamReader(in, "UTF-8")));
	}

	private void readLine(BufferedReader br) throws IOException {
		String line = br.readLine();
		setMethodAndPath(line);

		while (!"".equals(line)) {
			line = br.readLine();
			log.debug("header : " + line);
			if (line == null) {
				break;
			}
			headerToMap(line);
		}

		if (httpMap.get("Method") == "GET" & ((String) httpMap.get("URI")).contains("\\?")) {
			extractGetParam((String) httpMap.get("URI"));
		}

		String postParam = br.readLine();

		if (httpMap.get("Method") == "POST" & postParam != null) {
			extractPostParam(postParam);
		}
	}

	private void setMethodAndPath(String line) {
		String[] splitLine = HttpRequestUtils.splitString(line);
		httpMap.put("Method", splitLine[0]);
		httpMap.put("URI", splitLine[1]);
	}

	private void headerToMap(String line) {
		String[] splitLine = HttpRequestUtils.splitString(line);
		httpMap.put(splitLine[0], splitLine[1]);
	}

	private void extractGetParam(String line) {
		String[] splitGet = HttpRequestUtils.splitUrl(line);
		httpMap.put("URI", splitGet[0]);
		httpMap.put("Param", HttpRequestUtils.parseQueryString(splitGet[1]));
	}

	private void extractPostParam(String line) {
		httpMap.put("Param", HttpRequestUtils.parseQueryString(line));
	}

	public String getMethod() {
		return (String) httpMap.get("Method");
	}

	public String getURI() {
		return (String) httpMap.get("URI");
	}

	public Map<String, String> getParam() {
		return (Map<String, String>) httpMap.get("Param");
	}
}
