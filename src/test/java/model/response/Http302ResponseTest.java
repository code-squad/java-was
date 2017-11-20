package model.response;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Http302ResponseTest {
	HttpResponse response;
	
	@Before
	public void setup() {
		response = HttpResponseFactory.createHttpResponse(302);
	}
	
	@Test
	public void 리스폰스_헤더_확인() {
		String header = "HTTP/1.1 302 Found \r\nLocation: /index.html";
		response.setHeader("url", "/index.html");
		assertEquals(header,response.createHeader());
	}
}
