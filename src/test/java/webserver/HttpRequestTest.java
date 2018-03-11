package webserver;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
        List<String> requestHeader = httpRequest.getRequestHeader();
        assertEquals("GET /user/create?userId=chloe&password=password&name=JaeSung HTTP/1.1", requestHeader.get(0));
        assertEquals("Host: localhost:8080", requestHeader.get(1));
        assertEquals("Connection: keep-alive", requestHeader.get(2));
        assertEquals("Accept: */*", requestHeader.get(3));

    }

    @Test
    public void getURI() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        String uri = "/user/create?userId=chloe&password=password&name=JaeSung";
        assertEquals(uri, httpRequest.getURI());
    }

    @Test
    public void getRequestParameter() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        String uri = httpRequest.getURI();
        String queryString = httpRequest.getQueryString(uri);
        log.debug("uri : {}", uri);
        Map<String, String> parameters = httpRequest.getRequestParameter(queryString);
        assertEquals("chloe", parameters.get("userId"));
        assertEquals("password", parameters.get("password"));
        assertEquals("JaeSung", parameters.get("name"));
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