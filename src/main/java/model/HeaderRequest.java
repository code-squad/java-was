package model;

import java.util.ArrayList;

import util.SplitUtils;

public class HeaderRequest {
	String requestLine;
	ArrayList<String> headerLines = new ArrayList<String>();
	RequestPath path;
	
	//GET /index.html HTTP/1.1
	public HeaderRequest(String requestLine) {
		this.requestLine = requestLine;
		path = new RequestPath(SplitUtils.getSplitedValue(requestLine, " ", 1));
	}
	
	public void addLine(String headerLine) {
		headerLines.add(headerLine);
	}
	
	public RequestPath getRequestPath() {
		return path;
	}
	
	public String getPathValue() {
		return path.toString();
	}
}