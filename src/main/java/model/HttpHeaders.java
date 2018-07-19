package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeaders {
	private static final Logger log = LoggerFactory.getLogger(HttpHeaders.class);

	private Map<String,String> headers;
	
	public HttpHeaders() {
	}

	public HttpHeaders(Map<String,String> headers) {
		this.headers = headers;
	}
	
	public static HttpHeaders of(BufferedReader bufferReader) throws IOException{
		Map<String, String> headers = new HashMap<String, String>();
		String line = bufferReader.readLine();
		while (!"".equals(line)) {
			log.debug("header : {}", line);
			line = bufferReader.readLine();
			List<String> headerTokens = Arrays.asList(line.split(": "));
			if (headerTokens.size() == 2) {
				headers.put(headerTokens.get(0), headerTokens.get(1));
			}
		}
		return new HttpHeaders(headers);
	}

	public String findByHeaderName(String headerName) {
		if(headers.get(headerName)==null) {
			return "";
		}
		return headers.get(headerName).trim();
	}
	
}
