package util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class RequestHeaderUtilTest {

	
	@Test
	public void getAddress() throws IOException {
		String inputSteram = "GET /index.html HTTP/1.1";
		StringReader sr = new StringReader(inputSteram);
		BufferedReader br = new BufferedReader(sr);
		
		String line = br.readLine();
		
		assertThat(RequestHeaderUtil.getAddress(line), is("/index.html"));
	}

	@Test
	public void getParameter() throws IOException {
		String inputSteram = "GET /user/create?userId=gram&password=1234&name=gram&email=gram@naver.com HTTP/1.1";
		StringReader sr = new StringReader(inputSteram);
		BufferedReader br = new BufferedReader(sr);
		String line = br.readLine();
		assertThat(RequestHeaderUtil.parameterToQueryString(line), is("userId=gram&password=1234&name=gram&email=gram@naver.com"));
	}
	
}
