package request;

import util.HttpRequestUtils.RequestMethodType;
import util.SplitUtils;

public class RequestLine {
	private RequestPath requestPath;
	private RequestMethodType methodType;
	private String httpVersion;
	
	public RequestLine(String inputValue) {
		methodType = RequestMethodType.getRequestMethodType(SplitUtils.getSplitedValue(inputValue, " ", 0));
		requestPath = new RequestPath(SplitUtils.getSplitedValue(inputValue, " ", 1));
		httpVersion = SplitUtils.getSplitedValue(inputValue, " ", 2);
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