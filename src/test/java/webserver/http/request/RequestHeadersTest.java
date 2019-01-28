package webserver.http.request;

import com.google.common.net.HttpHeaders;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.BufferedReaderGenerator;
import util.HttpHeaderUtils;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class RequestHeadersTest {
    private static final Logger logger = LoggerFactory.getLogger(RequestHeadersTest.class);

    @Test
    public void getCookie() throws IOException {
        String value = "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        RequestHeaders headers = HttpHeaderUtils.parseRequestHeaders(br);

        Map<String, String> cookie = HttpRequestUtils.parseCookies(headers.getHeader("Cookie"));
        logger.debug("logined : {}", cookie.get("logined"));
        assertThat(cookie.get("logined"), is("true"));
    }

    @Test
    public void getAccepts() throws IOException {
        // "GET /user/list HTTP/1.1\n"
        String value = "Host: localhost:8080\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "Connection: keep-alive\n" +
                "Cookie: logined=true\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        RequestHeaders headers = HttpHeaderUtils.parseRequestHeaders(br);

        String header = headers.getHeader("Accept");
        assertTrue(header.startsWith("text/"));
    }
}