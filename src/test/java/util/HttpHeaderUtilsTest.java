package util;

import webserver.http.request.RequestHeaders;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.BufferedReaderGenerator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpHeaderUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpHeaderUtils.class);

    @Test
    public void parseHttpHeaders() throws IOException {
        String value = "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 59\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "\n";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);

        RequestHeaders requestHeaders = HttpHeaderUtils.parseRequestHeaders(br);
        assertThat(requestHeaders.getHeader("Host"), is("localhost:8080"));
        assertThat(requestHeaders.getHeader("Content-Length"), is("59"));
    }
}