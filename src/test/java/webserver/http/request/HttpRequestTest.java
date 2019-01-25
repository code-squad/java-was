package webserver.http.request;

import org.junit.Test;
import support.BufferedReaderGenerator;
import webserver.RequestGenerator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void isLogined_true() throws IOException {
        String value = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        HttpRequest request = RequestGenerator.generateRequest(br);

        assertTrue(request.isLogined());
    }

    @Test
    public void isLogined_false() throws IOException {
        String value = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Cookie: logined=false\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        HttpRequest request = RequestGenerator.generateRequest(br);

        assertFalse(request.isLogined());
    }

    @Test
    public void getAcceptType() throws IOException {
        String value = "GET /user/list HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "Connection: keep-alive\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        HttpRequest request = RequestGenerator.generateRequest(br);

        assertThat(request.getAcceptType(), is("text/html"));
    }
}