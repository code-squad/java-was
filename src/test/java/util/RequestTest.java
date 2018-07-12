package util;

import org.junit.Test;
import webserver.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static webserver.HttpMethod.POST;
import static webserver.HttpMethod.GET;

public class RequestTest {

    @Test
    public void indexHtml_GET() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/chaegyunjung/Documents/codesquad/java-was/http-requests/indexGet.http"));
        Request request = new Request(br);
        assertThat(request.getHttpMethod(), is(GET));
        assertThat(request.getHttpVersion(), is("HTTP/1.1"));
        assertThat(request.getPath(), is("/index.html"));
    }

    @Test
    public void createRequest_POST() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/chaegyunjung/Documents/codesquad/java-was/http-requests/userCreatePost.http"));
        Request request = new Request(br);
        Map<String, String> params = new HashMap<>();
        params.put("userId", "javajigi");
        params.put("password", "test");
        params.put("name", "hello");
        params.put("email", "hello@gmail.com");

        assertThat(request.getHttpMethod(), is(POST));
        assertThat(request.getHttpVersion(), is("HTTP/1.1"));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getParams(), is(params));
    }


}
