package util;

import webserver.http.request.RequestLine;
import org.junit.Test;
import support.BufferedReaderGenerator;
import vo.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpLineUtilsTest {
    @Test
    public void parseRequestLine_no_query() throws IOException {
        String value = "GET /index.html HTTP/1.1";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);

        RequestLine requestLine = HttpLineUtils.parseRequestLine(br);
        assertThat(requestLine.getMethod(), is(HttpMethod.GET));
        assertThat(requestLine.getUri(), is("/index.html"));
        assertThat(requestLine.getVersion(), is("HTTP/1.1"));
        assertThat(requestLine.getQuery().isEmpty(), is(true));
    }

    @Test
    public void parseRequestLine_has_query() throws IOException {
        String value = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);

        RequestLine requestLine = HttpLineUtils.parseRequestLine(br);
        assertThat(requestLine.getMethod(), is(HttpMethod.GET));
        assertThat(requestLine.getUri(), is("/user/create"));
        assertThat(requestLine.getVersion(), is("HTTP/1.1"));

        Map<String, String> query = requestLine.getQuery();
        assertThat(query.get("userId"), is("javajigi"));
    }
}