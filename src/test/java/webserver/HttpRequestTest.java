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
        List<String> requestMessage = httpRequest.getRequestMessage();
        assertEquals("GET /user/create?userId=chloe&password=password&name=JaeSung HTTP/1.1", requestMessage.get(0));
        assertEquals("Host: localhost:8080", requestMessage.get(1));
        assertEquals("Connection: keep-alive", requestMessage.get(2));
        assertEquals("Accept: */*", requestMessage.get(3));

    }

    @Test
    public void printAllHeader_post() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "postRequestMessage.txt"));
        httpRequest = new HttpRequest(in);

    }

    @Test
    public void getURI() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        String uri = "/user/create?userId=chloe&password=password&name=JaeSung";
        assertEquals(uri, httpRequest.getURI());
    }

    @Test
    public void getRequestParameter_get() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        String uri = httpRequest.getURI();
        log.debug("uri : {}", uri);
        Map<String, String> parameters = httpRequest.getRequestParameter(uri);
        assertEquals("chloe", parameters.get("userId"));
        assertEquals("password", parameters.get("password"));
        assertEquals("JaeSung", parameters.get("name"));
    }

}