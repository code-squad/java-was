package util;

import webserver.http.request.RequestBody;
import webserver.http.request.RequestHeaders;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.BufferedReaderGenerator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpBodyUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpBodyUtils.class);

    @Test
    public void parseRequestBody() throws IOException {
        String value = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 59\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        HttpLineUtils.parseRequestLine(br);
        RequestHeaders requestHeaders = HttpHeaderUtils.parseRequestHeaders(br);
        RequestBody requestBody = HttpBodyUtils.parseRequestBody(br, requestHeaders.getContentLength());


        assertThat(requestBody.getParameter("password"), is("password"));

    }
}