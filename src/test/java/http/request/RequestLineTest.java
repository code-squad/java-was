package http.request;

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
		requestLine = new RequestLine("GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1 ");
	}
	
	@Test
	public void getterMethodTest() {
		assertThat(requestLine.getMethod(), is(Method.GET));
	}
	
	@Test
	public void getterPathTest() {
		assertThat(requestLine.getPath(), is("/user/create"));
	}
	
	@Test
	public void getterQueryTest() {
		assertThat(requestLine.getQuery(), is("userId=javajigi&password=password&name=JaeSung"));
	}
	
	
	@Test
	public void MachMethodTest() {
		assertTrue(requestLine.matchMethod(Method.GET));
	}
	
	@Test(expected = NullPointerException.class)
	public void getParameterTest() {
		RequestLine requestLine = new RequestLine("GET /user/create HTTP/1.1 ");

		assertThat(requestLine.getParameter("userId"), is("javajigi"));
		assertThat(requestLine.getParameter("password"), is("password"));
		assertThat(requestLine.getParameter("name"), is("JaeSung"));
	}

}
