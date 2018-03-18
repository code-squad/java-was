package webserver;

public class RequestLine {
	private String method;
	private String uri;

	public RequestLine(String[] splitLine) {
		this.method = splitLine[0];
		this.uri = splitLine[1];
	}
	
	public String getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}
	
	public boolean isContains(String paramSeparator) {
		return uri.contains(paramSeparator);
	}
	
	public String splitUriParam(String[] splitGet) {
		uri = splitGet[0];
		return splitGet[1];
	}
}
