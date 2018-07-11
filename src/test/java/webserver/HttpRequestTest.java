package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpRequestTest {
    private static final String REQUEST_LINE = "GET /user/create?userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net";
    private static final String REQUEST_HEADER = "Host: localhost:8080";
    private HttpRequest request;

    @Before
    public void setUp() throws Exception {
        InputStream in = new FileInputStream(new File(getClass().getClassLoader().getResource("getCreateUser.txt").getFile()));
        request = new HttpRequest(in);
    }

    @Test
    public void getPath() throws Exception {
        assertThat(request.getPath(), is("/user/create"));
    }

    @Test
    public void getParameters_GET_Request() throws Exception {
        Map<String, String> params = request.getParameters();
        assertThat(params.get("userId"), is("javajigi"));
        assertThat(params.get("password"), is("password"));
        assertThat(params.get("name"), is("박재성"));
        assertThat(params.get("email"), is("javajigi@slipp.net"));
    }

    @Test
    public void getParameters_POST_Request() throws Exception {
        InputStream in = new FileInputStream(new File(getClass().getClassLoader().getResource("postCreateUser.txt").getFile()));
        request = new HttpRequest(in);
        Map<String, String> params = request.getParameters();
        assertThat(params.get("userId"), is("javajigi"));
        assertThat(params.get("password"), is("password"));
        assertThat(params.get("name"), is("박재성"));
        assertThat(params.get("email"), is("javajigi@slipp.net"));
    }

    @Test
    public void getBody_GET_Request() {
        assertThat(request.getBody().toString(), is(""));
    }

    @Test
    public void getBody_POST_Request() throws Exception {
        InputStream in = new FileInputStream(new File(getClass().getClassLoader().getResource("postCreateUser.txt").getFile()));
        request = new HttpRequest(in);
        assertThat(request.getBody().toString(), is("userId=javajigi&password=password&name=박재성&email=javajigi@slipp.net"));
    }
}