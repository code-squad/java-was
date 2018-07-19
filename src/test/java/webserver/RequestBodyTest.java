package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class RequestBodyTest {
    private static final String BODY_STRING = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
    private static final String DECODED_STRING = "userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net";
    private RequestBody requestBody;

    @Before
    public void setUp() throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(BODY_STRING));

    }

    @Test
    public void of() {
        requestBody = RequestBody.of(BODY_STRING);
        assertEquals(DECODED_STRING, requestBody.toString());
        assertEquals("javajigi", requestBody.getParameters().get("userId"));
        assertEquals("password", requestBody.getParameters().get("password"));
        assertEquals("박재성", requestBody.getParameters().get("name"));
        assertEquals("javajigi@slipp.net", requestBody.getParameters().get("email"));
    }

    @Test
    public void ofEmpty() {
        requestBody = RequestBody.ofEmpty();
        assertEquals("", requestBody.toString());
    }
}