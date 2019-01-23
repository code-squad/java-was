package util;

import http.RequestHeaders;
import org.junit.Test;
import support.BufferedReaderGenerator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpHeaderUtilsTest {

    @Test
    public void parseHttpHeaders() throws IOException {
        String value = "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n\n" +
                "";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);

        RequestHeaders requestHeaders = HttpHeaderUtils.parseRequestHeaders(br);
        assertThat(requestHeaders.getHeader("Host"), is("localhost:8080"));

    }
}