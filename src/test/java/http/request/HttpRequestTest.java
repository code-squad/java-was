package http.request;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import http.Method;
import http.request.HttpRequest;

public class HttpRequestTest {
	private String testDirectory = "./src/test/java/resources/";

	@Test
	public void requestGETTEST() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
		HttpRequest request = new HttpRequest(in);

		assertEquals(Method.GET, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("JaeSung", request.getParameter("name"));
		assertEquals("javajigi", request.getParameter("userId"));
	}

	@Test
	public void requestPOSTTEST() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
		HttpRequest request = new HttpRequest(in);

		assertEquals(Method.POST, request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("javajigi", request.getParameter("userId"));
	}

}
