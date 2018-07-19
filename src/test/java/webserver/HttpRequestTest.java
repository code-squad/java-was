package webserver;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws IOException {
        InputStream inputStream = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = HttpFactory.init(inputStream);

        assertEquals("GET", request.getMethod());
        assertEquals("/user/create", request.getUrl());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParams("userId"));
    }

    @Test
    public void request_POST() throws IOException {
        InputStream inputStream = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        HttpRequest request = HttpFactory.init(inputStream);

        assertEquals("POST", request.getMethod());
        assertEquals("/user/create", request.getUrl());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParams("userId"));
    }
}
