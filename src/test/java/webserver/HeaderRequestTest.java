package webserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HeaderRequestTest {
	@Test
	public void requestLineTest() {
		HeaderRequest request = new HeaderRequest("GET /index.html HTTP/1.1");
		assertEquals("/index.html", request.getPath());
	}
}
