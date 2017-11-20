package model.response;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Http200ResponseTest {
	HttpResponse response;
	
	@Before
	public void setup() {
		response = HttpResponseFactory.createHttpResponse(200);
	}
	
	@Test
	public void 리스폰스_헤더_확인() {
		String header = "HTTP/1.1 200 OK \r\nContent-Type: */*\r\nContent-Length: 50\r\n\r\n";
		response.setHeader("Content-Type", "*/*");
		response.setHeader("Content-Length", "50");
		assertEquals(header,response.createHeader());
	}

}
