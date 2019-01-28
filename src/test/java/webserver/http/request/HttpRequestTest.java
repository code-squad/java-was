package webserver.http.request;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.BufferedReaderGenerator;
import vo.HttpMethod;
import webserver.RequestGenerator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpRequestTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestTest.class);

    @Test
    public void isLogined_true() throws IOException {
        String message = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(message);
        HttpRequest request = RequestGenerator.generateRequest(br);

        String result = request.getHeader("Cookie");
        assertThat(result, is("logined=true"));
    }

    @Test
    public void isLogined_false() throws IOException {
        String message = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Cookie: logined=false\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(message);
        HttpRequest request = RequestGenerator.generateRequest(br);

        String result = request.getHeader("Cookie");
        assertThat(result, is("logined=false"));
    }

    @Test
    public void getAcceptType() throws IOException {
        String message = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "Connection: keep-alive\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(message);
        HttpRequest request = RequestGenerator.generateRequest(br);

        String result = request.getHeader("Accept");
        logger.debug("Accept : {}", result);
        assertTrue(request.getHeader("Accept").startsWith("text"));
    }

    @Test
    public void matchMethod() throws IOException {
        String message = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "Connection: keep-alive\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(message);
        HttpRequest request = RequestGenerator.generateRequest(br);

        assertTrue(request.matchMethod(HttpMethod.GET));
        assertFalse(request.matchMethod(HttpMethod.POST));
    }

    @Test
    public void post_with_query() throws IOException {
        String message = "POST /user/create?id=1 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 46\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=JaeSung";

        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(message);
        HttpRequest request = RequestGenerator.generateRequest(br);

        assertTrue(request.matchMethod(HttpMethod.POST));
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("1", request.getParameter("id"));
        assertEquals("javajigi", request.getParameter("userId"));
    }
}