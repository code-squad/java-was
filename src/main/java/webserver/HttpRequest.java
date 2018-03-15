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
	private Map<String, String> httpMap = new HashMap<>();
	private Map<String, String> param = new HashMap<>();
	
	public HttpRequest(InputStream in) throws IOException {
		readLine(new BufferedReader(new InputStreamReader(in, "UTF-8")));
	}

	private void readLine(BufferedReader br) throws IOException {
		String line = br.readLine();
		log.debug("FIRST LINE : " + line);
		setMethodAndPath(line);
		line = repeatReadLine(br, line);
		String method = httpMap.get("Method");
		String uri = httpMap.get("URI");

		if (HttpMethod.GET.whichMethod(method) & uri.contains("?")) {
			// POST 방식에서 쿼리스트링 들어오는거 받을 수 있는지 테스트
			extractGetParam(uri);
		}

		if (HttpMethod.POST.whichMethod(method) & httpMap.get("Content-Length") != null) {
			createParam(IOUtils.readData(br, Integer.parseInt(httpMap.get("Content-Length"))));
		}
	}
	
	private void setMethodAndPath(String line) {
		String[] splitLine = HttpRequestUtils.splitStringBlank(line);
		httpMap.put("Method", splitLine[0]);
		httpMap.put("URI", splitLine[1]);
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
			httpMap.put(splitLine[0], splitLine[1]);
		}
	}
		
	private void createParam(String line) {
		param = HttpRequestUtils.parseQueryString(line);
	}

	private void extractGetParam(String line) {
		String[] splitGet = HttpRequestUtils.splitUrl(line);
		httpMap.put("URI", splitGet[0]);
		createParam(splitGet[1]);
	}

	public String getMethod() {
		return httpMap.get("Method");
	}

	public String getURI() {
		return httpMap.get("URI");
	}

	public Map<String, String> getParam() {
		return param;
	}

	public String getLogined() {
		if (httpMap.get("logined") == null) {
			return "logined=false;";
		}
		return httpMap.get("logined");
	}

	public String getAccept() {
		return httpMap.get("Accept");
	}
	
	public String getCookie() {
		return httpMap.get("Cookie");
	}
}
