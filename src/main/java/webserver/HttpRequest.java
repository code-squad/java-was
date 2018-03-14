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
	private Map<String, Object> httpMap = new HashMap<>();

	public HttpRequest(InputStream in) throws IOException {
		readLine(new BufferedReader(new InputStreamReader(in, "UTF-8")));
	}

	private void readLine(BufferedReader br) throws IOException {
		String line = br.readLine();
		log.debug("FIRST LINE : " + line);
		setMethodAndPath(line);
		line = repeatReadLine(br, line);
		String method = (String) httpMap.get("Method");
		String uri = (String) httpMap.get("URI");

		if (method.equals("GET") & uri.contains("?")) {
			extractGetParam(uri);
		}

		if (method.equals("POST") & httpMap.get("Content-Length") != null) {
			extractPostParam(IOUtils.readData(br, Integer.parseInt((String) httpMap.get("Content-Length"))));
		}
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

	private void setMethodAndPath(String line) {
		String[] splitLine = HttpRequestUtils.splitStringBlank(line);
		httpMap.put("Method", splitLine[0]);
		httpMap.put("URI", splitLine[1]);
	}

	private void headerToMap(String line) {
		String[] splitLine = HttpRequestUtils.splitString(line);
		if (!"".equals(splitLine[0])) {
			httpMap.put(splitLine[0], splitLine[1]);
		}
		if (!"".equals(splitLine[0]) & splitLine[0].equals("Cookie")) {
			checkLogined(splitLine);
		}
	}

	private void checkLogined(String[] splitLine) {
		for (String string : splitLine) {
			if (string.contains("true")) {
				httpMap.put("logined", "logined=true;");
			}
			if (string.contains("false")) {
				httpMap.put("logined", "logined=false;");
			}
		}
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

	public String getLogined() {
		if (httpMap.get("logined") == null) {
			return "logined=false;";
		}
		return (String) httpMap.get("logined");
	}

	public String getAccept() {
		return (String) httpMap.get("Accept");
	}
}
