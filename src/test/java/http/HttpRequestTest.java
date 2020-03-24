package http;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpRequestTest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestTest.class);
    private static final String testDirectory = "./src/test/resources/";
    HttpRequest httpRequest;

    @Before
    public void request객체생성() {
        InputStream in = null;
        try {
//            in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
            in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        httpRequest = new HttpRequest(br);

    }

    @Test
    public void getMethod() {
        HttpMethod post = HttpMethod.POST;
        assertEquals(post, httpRequest.getMethod());
    }

    @Test
    public void getPath() {
        String path = "/user/create";
        assertEquals(path, httpRequest.getPath());
    }

    @Test
    public void getHeader() {
        assertEquals("localhost:8080", httpRequest.getHeader("Host"));
    }

    @Test
    public void getParameter() {
        assertEquals("spring", httpRequest.getParameter("userId"));
    }
}
