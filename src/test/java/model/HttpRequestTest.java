package model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestTest {
	private static final Logger log = LoggerFactory.getLogger(HttpRequestTest.class);
	private String testDirectory = "./src/test/java/";
	
	
	@Test
	public void makeHeader() throws IOException {
        InputStream inputSteram = new FileInputStream(new File( testDirectory + "model/Http_GET.txt"));
    	HttpRequest httpRequest = HttpRequest.of(inputSteram);
    	log.debug("httprequest : {}",httpRequest.toString());
        assertThat(httpRequest.getMethod(), is("GET"));
        assertThat(httpRequest.getParameter("userId"), is("lsc109"));
        assertThat(httpRequest.getUrl(), is("/user/create?userId=lsc109&password=1234&name=gram"));
        assertThat(httpRequest.getHeader("Connection"), is("keep-alive"));
	}

	
	
}
