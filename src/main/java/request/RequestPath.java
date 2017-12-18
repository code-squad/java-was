package request;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		RequestPath other = (RequestPath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}