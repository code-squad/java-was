package http.response;

import static org.junit.Assert.*;

import org.junit.Test;

public class HttpResponseFactoryTest {

	@Test
	public void get200Response() {
		assertTrue(HttpResponseFactory.getResponse(Status.NUMBER200) instanceof Http200Response);
	}
	
	@Test
	public void get302Response() {
		assertTrue(HttpResponseFactory.getResponse(Status.NUMBER302) instanceof Http302Response);
	}

}
