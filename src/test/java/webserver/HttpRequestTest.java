package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThat;

public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";
    public HttpRequest httpRequest;

    @Test
    public void getRequestParameter_post() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "postRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        Map<String, String> parameters = httpRequest.getRequestParameter();
        assertEquals("javajigi", parameters.get("userId"));
        assertEquals("password", parameters.get("password"));
        assertEquals("javajigi%40slipp.net", parameters.get("email"));
    }

    @Test
    public void getRequestParameter_get() throws Exception{
        InputStream in = new FileInputStream(new File(testDirectory + "getRequestMessage.txt"));
        httpRequest = new HttpRequest(in);
        Map<String, String> parameters = httpRequest.getRequestParameter();
        assertEquals("chloe", parameters.get("userId"));
        assertEquals("password", parameters.get("password"));
        assertEquals("JaeSung", parameters.get("name"));
    }

}