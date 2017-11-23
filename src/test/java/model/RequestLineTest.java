package model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RequestLineTest {
	RequestLine requestLine;
	
	@Before
	public void setup() {
		requestLine = new RequestLine("POST /create/user");
	}
	
	@Test
	public void getterCheckTest() {
		assertThat(requestLine.getUrl(), is("/create/user"));
		assertThat(requestLine.getMethod(), is(Method.POST));
	}
	
	@Test
	public void MachMethodTest() {
		assertTrue(requestLine.matchMethod(Method.POST));
	}

}
