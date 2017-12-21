package request;

import java.util.ArrayList;

public class ResponseHeaderValues {
	private ArrayList<ResponseHeader> responseHeaders = new ArrayList<ResponseHeader>();

	public void addResponseHeaderValues(ResponseHeader responseHeader) {
		responseHeaders.add(responseHeader);
	}

	public void addResponseHeaderValues(ResponseHeaderValues responseHeaderValue) {
		responseHeaders.addAll(responseHeaderValue.getResponseHeaders());
	}

	public ArrayList<ResponseHeader> getResponseHeaders() {
		return responseHeaders;
	}
}
