package webserver;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestLineTest {
    private static final Logger logger = LoggerFactory.getLogger(RequestLineTest.class);

    private static final String SAMPLE_LINE = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
    private static final String URI = "/user/create";
    private static final String QUERY_STRING = "userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net";
    private RequestLine requestLine;

    @Before
    public void setUp() throws Exception {
        StringReader sr = new StringReader(SAMPLE_LINE);
        BufferedReader reader = new BufferedReader(sr);
        requestLine = new RequestLine(reader);
    }

    @Test
    public void getPath() {
        assertEquals(URI, requestLine.getPath());
    }

    @Test
    public void getQueryString() {
        assertEquals(QUERY_STRING, requestLine.getQueryString());
    }

    @Test
    public void getQueryParameters() {
        Map<String, String> params = requestLine.getQueryParameters();
        assertEquals("javajigi", params.get("userId"));
        assertEquals("password", params.get("password"));
        assertEquals("박재성", params.get("name"));
        assertEquals("javajigi@slipp.net", params.get("email"));
    }
}