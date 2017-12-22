package request;

import util.HttpRequestUtils.RequestMethodType;

public class RequestLine {
	private RequestPath requestPath;
	private RequestMethodType methodType;
	private String httpVersion;
	
	public RequestLine(String inputValue) {
		String[] splitedValue = inputValue.split(" ");
		methodType = RequestMethodType.getRequestMethodType(splitedValue[0]);
		requestPath = new RequestPath(splitedValue[1]);
		httpVersion = splitedValue[2];
	}
	
	public RequestPath getRequestPath() {
		return requestPath;
	}
	
	public RequestMethodType getMethodType() {
		return methodType;
	}
	
	public String getHttpVersion() {
		return httpVersion;
	}
}