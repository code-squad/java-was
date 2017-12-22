package request;

import util.HttpRequestUtils.RequestMethodType;

public class RequestHeader {
	private RequestLine requestLine;	
	private GeneralHeaderValue requestHeaderValues = new GeneralHeaderValue();	
	private String requestBody = "no body content";
	private int contentLength = 0;

	// GET /index.html HTTP/1.1
	public RequestHeader(String inputLine) {
		this.requestLine = new RequestLine(inputLine);
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	public GeneralHeaderValue getRequestHeaderValues(){
		return requestHeaderValues;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void addLine(String headerLine) {
		requestHeaderValues.addGeneralHeaderValue(headerLine);
		// 콘텐츠 확인
		if (headerLine.startsWith("Content-Length:")) {
			contentLength = Integer.parseInt(headerLine.substring("Content-Length:".length()).trim());
		}
	}

	public RequestPath getRequestPath() {
		return requestLine.getRequestPath();
	}

	public int getContentLength() {
		return contentLength;
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public RequestMethodType getRequestMethodType() {
		return requestLine.getMethodType();
	}

	public String getPathValue() {
		return getRequestPath().toString();
	}
}