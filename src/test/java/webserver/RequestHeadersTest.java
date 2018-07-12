package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class RequestHeadersTest {
    private static final String HEADERS = "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 93\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Accept: */*";
    private RequestHeaders requestHeaders;

    @Before
    public void setUp() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(HEADERS));
        requestHeaders = new RequestHeaders(reader);
    }

    @Test
    public void containsHeader() {
        assertTrue(requestHeaders.containsHeader(HttpHeader.HOST));
        assertTrue(requestHeaders.containsHeader(HttpHeader.CONNECTION));
        assertTrue(requestHeaders.containsHeader(HttpHeader.CONTENT_LENGTH));
        assertTrue(requestHeaders.containsHeader(HttpHeader.CONTENT_TYPE));
        assertTrue(requestHeaders.containsHeader(HttpHeader.ACCEPT));

        assertFalse(requestHeaders.containsHeader(HttpHeader.AUTHORIZATION));
    }

    @Test
    public void getHeader() {
        assertEquals("localhost:8080", requestHeaders.getHeader(HttpHeader.HOST));
        assertEquals("keep-alive", requestHeaders.getHeader(HttpHeader.CONNECTION));
        assertEquals("93", requestHeaders.getHeader(HttpHeader.CONTENT_LENGTH));
    }
}