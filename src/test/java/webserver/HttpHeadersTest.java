package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HttpHeadersTest {

    private HttpHeaders headers;

    @Before
    public void setUp() throws Exception {
        headers = new HttpHeaders();
    }

    @Test
    public void buildHeaders() {
        headers.addHeader(HttpHeader.CONTENT_TYPE, "utf-8");
        headers.addHeader(HttpHeader.CONTENT_LENGTH, "70");

        assertTrue(headers.containsHeader(HttpHeader.CONTENT_TYPE));
        assertTrue(headers.containsHeader(HttpHeader.CONTENT_LENGTH));
        assertThat(headers.buildHeaders(), is("Content-Type: utf-8\r\nContent-Length: 70\r\n"));
    }
}