package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import util.HttpRequestUtils;
import util.IOUtils;

public class Parameter {

	private Map<String, String> params;
	
	public Parameter() {
	}

	public Parameter(Map<String, String> params) {
		this.params = params;
	}
	
	public static Parameter of(BufferedReader bufferReader, String contentLength) throws NumberFormatException, IOException {
		 String parmeter =  IOUtils.readData(bufferReader, Integer.parseInt(contentLength));
		return new Parameter(HttpRequestUtils.parseQueryString(parmeter));
	}
	public static Parameter of(String url) throws NumberFormatException, IOException {
		return new Parameter(HttpRequestUtils.parseQueryString(url.substring(url.indexOf("?") + 1)));
	}

	public Map<String, String> getParams() {
		return params;
	}
	
	public String getParameter(String name) {
		return params.get(name);
	}

	
}
