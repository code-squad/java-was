package model;

import java.util.Map;

import util.HttpRequestUtils;

public class RequestPath {
	String path;
	String url;
	Map<String, String> inputValues;

	public RequestPath(String path) {
		this.path = path;
		parseUrlAndInputValue(path);
	}

	public String getOnlyUrl() {
		return url;
	}

	public Map<String, String> getInputValue() {
		return inputValues;
	}

	private void parseUrlAndInputValue(String inputPath) {
		String[] tokens = inputPath.split("\\?");
		url = tokens[0];
		if (tokens.length > 1)
			inputValues = HttpRequestUtils.parseQueryString(tokens[1]);
	}

	@Override
	public String toString() {
		return path;
	}

}