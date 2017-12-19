package request;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import request.RequestHeader;

public class RequestHeaderTest {
	@Test
	public void requestLineTest() {
		RequestHeader request = new RequestHeader("GET /index.html HTTP/1.1");
		assertEquals("/index.html", request.getPathValue());
	}
}