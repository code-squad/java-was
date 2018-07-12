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
        assertTrue(requestHeaders.containsHeader("Host"));
        assertTrue(requestHeaders.containsHeader("Connection"));
        assertTrue(requestHeaders.containsHeader("Content-Length"));
        assertTrue(requestHeaders.containsHeader("Content-Type"));
        assertTrue(requestHeaders.containsHeader("Accept"));

        assertFalse(requestHeaders.containsHeader("Authorization"));
    }

    @Test
    public void getHeader() {
        assertEquals("localhost:8080", requestHeaders.getHeader("Host"));
        assertEquals("keep-alive", requestHeaders.getHeader("Connection"));
        assertEquals("93", requestHeaders.getHeader("Content-Length"));
    }
}