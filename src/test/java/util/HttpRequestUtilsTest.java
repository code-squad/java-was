package util;

import domain.HttpRequest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

import java.io.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class HttpRequestUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtilsTest.class);

    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is("password2"));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined"), is("true"));
        assertThat(parameters.get("JSessionId"), is("1234"));
        assertThat(parameters.get("session"), is(nullValue()));
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair, is(new Pair("userId", "javajigi")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }

    @Test
    public void parseHeader_PATH() {
        assertThat(HttpRequestUtils.parseUrl("GET /index.html HTTP/1.1"), is("/index.html"));
    }

    @Test
    public void readFile() throws IOException {
        String rootLocation = "./webapp";
        String path = HttpRequestUtils.parseUrl("GET /index.html HTTP/1.1");

        byte[] body = body = HttpRequestUtils.readFile(path);
        assertNotNull(body);
    }

    @Test
    public void parsePath_requestLine() {
        String line = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        assertThat(HttpRequestUtils.parsePath(line), is("/user/create"));
    }

    @Test
    public void parseCookie() {
        String cookieQueryString = "Idea-d506421b=a23c0e80-12ac-450d-917b-8429ed0cde6f; logined=true";
        Map<String, String> cookieData = HttpRequestUtils.parseCookies(cookieQueryString);

        assertThat(cookieData.get("logined"), is("true"));
    }

    @Test
    public void mapNull() {
        Map<String, String> loginData = new HashMap<>();
        if (loginData.get("loginUser") == null) {
            System.out.println("yes is null");
        }
    }

    @Test
    public void parseAccpet() {
        String line = "Accept: text/css,image/apng,image/*,*/*;q=0.8";
        String accpetValue = HttpRequestUtils.parseHeader(line).getValue();

        assertThat(HttpRequestUtils.parseAccept(accpetValue)[0], is("text/css"));
    }

    @Test
    public void parseParameter() {
        String line = "GET /user/create?userId=javajigi&password=password HTTP/1.1\r\n";
        assertThat(HttpRequestUtils.parseParameter(line), is("userId=javajigi&password=password"));
    }

//    @Test
//    public void getHttpRequest_GET() {
//        String line = "GET /user/create?userId=javajigi&password=password HTTP/1.1\r\n"
//                + "Host: localhost:8080\r\n"
//                + "Content-Length: 345\r\n"
//                + "\r\n";
//
//        InputStream is = new ByteArrayInputStream(line.getBytes());
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        HttpRequest request = null;
//
//        try {
//            request = HttpRequestUtils.getHttpRequest(reader);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        assertNotNull(request);
//        assertThat(request.getMethod(), is("GET"));
//        assertThat(request.getPath(), is("/user/create"));
//        assertThat(request.getParameter(), is("userId=javajigi&password=password"));
//        assertThat(request.getParameter("userId"), is("javajigi"));
//    }

    @Test
    public void saveHeader() {
        String contentLength = "Content-Length: 87";
        List<Pair> headers = new ArrayList<>();
        headers.add(HttpRequestUtils.parseHeader(contentLength));

        assertThat(headers.get(0).getKey(), is("Content-Length"));
        assertThat(headers.get(0).getValue(), is("87"));
    }
}
