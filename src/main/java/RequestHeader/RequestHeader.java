package RequestHeader;

import java.util.ArrayList;

import util.HttpRequestUtils.RequestMethodType;

public class RequestHeader {
	ArrayList<String> headerLines = new ArrayList<String>();
	RequestLine requestLine;
	String requestBody = "no body content";
	int contentLength = 0;

	// GET /index.html HTTP/1.1
	public RequestHeader(String inputLine) {
		this.requestLine = new RequestLine(inputLine);
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void addLine(String headerLine) {
		headerLines.add(headerLine);
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