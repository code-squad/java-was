package domain;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpRequestTest {
    private InputStream in;

    @Before
    public void setUp() {
        StringBuilder sb = new StringBuilder();
        sb.append("GET /user/form.html?name=learner HTTP/1.1").append("\r\n")
                .append("Cookie: logined=false").append("\r\n")
                .append("\r\n");
        in = new ByteArrayInputStream(sb.toString().getBytes());
    }

    @Test
    public void canCreate() throws IOException {
        Socket socket = new Socket();
        HttpRequest request = new HttpRequest(socket.getInputStream());
    }

    @Test
    public void parseRequestData() throws IOException {
        HttpRequest request = new HttpRequest(in);
        assertThat(request.getRequestLineData().get("method"), is("GET"));
        assertThat(request.getRequestLineData().get("path"), is("/user/form.html"));
        assertThat(request.getRequestLineData().get("parameter"), is("name=learner"));
        assertThat(request.getRequestLineData().get("version"), is("HTTP/1.1"));
    }

    @Test
    public void matchPath() throws IOException {
        HttpRequest request = new HttpRequest(in);
        assertThat(request.matchPath("/user/form.html"), is(true));
    }

    @Test
    public void matchCookieValue() throws IOException {
        HttpRequest request = new HttpRequest(in);
        assertThat(request.matchCookieValue("logined", "false"), is(false));
    }
}
