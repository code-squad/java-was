package request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import request.RequestLine;
import request.RequestPath;
import util.HttpRequestUtils.RequestMethodType;

public class RequestLineTest {
	RequestLine requestLine;
	
	@Before
	public void init() {
		requestLine = new RequestLine("GET / HTTP/1.1"); 
	}

	@Test
	public void requestLineTest() {
		assertEquals(RequestMethodType.GET, requestLine.getMethodType());
		assertEquals("GET", requestLine.getMethodType().toString());
		assertEquals("/", requestLine.getRequestPath().toString());
		assertTrue(new RequestPath("/").equals(requestLine.getRequestPath()));
		assertEquals("HTTP/1.1", requestLine.getHttpVersion());
	}
}