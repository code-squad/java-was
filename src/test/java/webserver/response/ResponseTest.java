package webserver.response;

import org.junit.Before;
import org.junit.Test;
import support.TestOutputStream;
import webserver.HttpStatus;

import java.io.IOException;

import static webserver.response.ResponseHeaderAttribute.CONTENT_TYPE;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() throws Exception {
        response = new Response(new TestOutputStream());
    }

    @Test
    public void response() throws IOException {
        response.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8").setStatus(HttpStatus.OK).setBody("");
        response.response();
    }
}