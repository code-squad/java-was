package model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import http.Method;
import http.request.RequestLine;

public class RequestLineTest {
	RequestLine requestLine;
	
	@Before
	public void setup() {
		requestLine = new RequestLine("POST /create/user?userId=javajigi&password=password&name=JaeSung");
	}
	
	@Test
	public void getterTest() {
		assertThat(requestLine.getMethod(), is(Method.POST));
		assertThat(requestLine.getPath(), is("/create/user"));
		assertThat(requestLine.getQuery(), is("userId=javajigi&password=password&name=JaeSung"));
	}
	
	@Test
	public void MachMethodTest() {
		assertTrue(requestLine.matchMethod(Method.POST));
	}

}
