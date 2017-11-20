package model.response;

import static org.junit.Assert.*;

import org.junit.Test;

public class HttpResponseFactoryTest {

	@Test
	public void respnse200을_만들어보자() {
		HttpResponse response = HttpResponseFactory.createHttpResponse(200);
		assertTrue(response instanceof Http200Response);
	}
	
	@Test
	public void respnse302을_만들어보자() {
		HttpResponse response = HttpResponseFactory.createHttpResponse(302);
		assertTrue(response instanceof Http302Response);
	}

}
