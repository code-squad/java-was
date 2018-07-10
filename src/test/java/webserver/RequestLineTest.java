package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestLineTest {
    private static final String SAMPLE_LINE = "GET /user/create?userId=javajigi&password=password HTTP/1.1";
    private RequestLine requestLine;

    @Before
    public void setUp() {
        requestLine = new RequestLine(SAMPLE_LINE);
    }

    @Test
    public void parseUrl() {
        String[] expected = new String[]{"/user/create", "userId=javajigi&password=password"};
        assertArrayEquals(expected, requestLine.parseUrl());
    }

    @Test
    public void getResource() {
        assertEquals("/user/create", requestLine.getResource().toString());
    }

    @Test
    public void getQueryString() {
        assertEquals("userId=javajigi&password=password", requestLine.getQueryString());
    }
}