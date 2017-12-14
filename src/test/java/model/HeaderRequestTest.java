package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.HeaderRequest;

public class HeaderRequestTest {
	@Test
	public void requestLineTest() {
		HeaderRequest request = new HeaderRequest("GET /index.html HTTP/1.1");
		assertEquals("/index.html", request.getPathValue());
	}
}