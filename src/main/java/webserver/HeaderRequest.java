package webserver;

import java.util.ArrayList;

public class HeaderRequest {
	String requestLine;
	ArrayList<String> headerLines = new ArrayList<String>();
	
	//GET /index.html HTTP/1.1
	public HeaderRequest(String requestLine) {
		this.requestLine = requestLine;
	}
	
	public void addLine(String headerLine) {
		headerLines.add(headerLine);
	}
	
	public String getPath() {
		String[] tokens = requestLine.split(" ");
		return tokens[1];
	}
}
