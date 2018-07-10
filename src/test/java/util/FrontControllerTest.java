package util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import webserver.FrontController;

public class FrontControllerTest {

	
	@Test
	public void indexHtml() throws IOException {
		String inputSteram = "GET /index.html HTTP/1.1";
		StringReader sr = new StringReader(inputSteram);
		BufferedReader br = new BufferedReader(sr);
		assertThat(FrontController.doProcess(br), is("/index.html"));
	}

	@Test
	public void createUser() throws IOException {
		String inputSteram = 
				"POST /user/create HTTP/1.1\r\n" + 
				"Host: localhost:8080\r\n" + 
				"Connection: keep-alive\r\n" + 
				"Content-Length: 59\r\n" + 
				"Content-Type: application/x-www-form-urlencoded\r\n" + 
				"Accept: */*\r\n" + 
				"\r\n" + 
				"userId=javajigi&password=password&name=gram&email=javajigi@40slipp.net";
		StringReader sr = new StringReader(inputSteram);
		BufferedReader br = new BufferedReader(sr);
		assertThat(FrontController.doProcess(br), is("/index.html"));
	}
	
}
