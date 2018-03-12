package webserver;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;

public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";
    public HttpRequest httpRequest;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @Test
    public void printAllHeader_get() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("GET /user/create?userId=chloe&password=password&name=JaeSung HTTP/1.1", httpRequest.getHeader("requestLine"));
        assertEquals("localhost:8080",  httpRequest.getHeader("Host"));
        assertEquals("keep-alive",  httpRequest.getHeader("Connection"));
        assertEquals("*/*",  httpRequest.getHeader("Accept"));

    }

    @Test
    public void printAllHeader_post() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "postRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net", httpRequest.getRequestBody());
    }

    @Test
    public void getMethod() throws IOException {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("GET", httpRequest.getMethod());
    }

    @Test
    public void getPath() throws IOException {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("/user/create", httpRequest.getPath());
    }

    @Test
    public void getParameter() throws IOException {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("chloe", httpRequest.getParameter("userId"));
        assertEquals("password", httpRequest.getParameter("password"));
        assertEquals("JaeSung", httpRequest.getParameter("name"));

    }

    @Test
    public void getRequestBody() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "postRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        String requestBody = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        assertEquals(requestBody, httpRequest.getRequestBody());
    }

    @Test
    public void getCookieValue() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessageWithCookie.txt"));
        httpRequest = new HttpRequest(in);
        boolean loginStatus = true;
        assertEquals(httpRequest.getCookieValue(), loginStatus);
    }

    @Test
    public void getContentType() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessageWithCookie.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("*/*", httpRequest.getContentType());
    }

    @Test
    public void getContentType2() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "postRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals("*/*", httpRequest.getContentType());
    }

    @Test
    public void getContentLength() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "postRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        assertEquals(93, httpRequest.getContentLength());
    }
}