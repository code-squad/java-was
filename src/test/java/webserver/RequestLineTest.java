package webserver;

import model.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class RequestLineTest {
    private static final Logger logger = LoggerFactory.getLogger(RequestLineTest.class);

    private static final String SAMPLE_LINE = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
    private static final String SAMPLE_URI = "/user/create";
    private static final String SAMPLE_QUERYSTRING = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
    private RequestLine requestLine;

    @Before
    public void setUp() {
        requestLine = new RequestLine(SAMPLE_LINE);
    }

    @Test
    public void parseUrl() {
        String[] expected = new String[]{SAMPLE_URI, SAMPLE_QUERYSTRING};
        assertArrayEquals(expected, requestLine.parseUrl());
    }

    @Test
    public void getResource() {
        assertEquals(SAMPLE_URI, requestLine.getResource().toString());
    }

    @Test
    public void getQueryString() {
        assertEquals(SAMPLE_QUERYSTRING, requestLine.getQueryString());
    }

    @Test
    public void getUser() {
        User expected = new User("javajigi", "password", "박재성", "javajigi@slipp.net");
        User actual = requestLine.getUser();
        assertEquals(expected.toString(), actual.toString());
        logger.debug(actual.toString());
    }
}