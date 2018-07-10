package webserver;

import exception.HeaderNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestTest {
    private static final Logger log = LoggerFactory.getLogger(RequestTest.class);

    private Request request;
    private Request signRequest;

    @Before
    public void setUp() throws Exception {
        request = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("request.txt").getFile())));
        signRequest = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("signRequest.txt").getFile())));
    }

    @Test
    public void readPath() {
        assertThat(request.getPath(), is("/index.html"));
    }

    @Test
    public void readMethod() {
        assertThat(request.getMethod(), is("GET"));
    }

    @Test
    public void readHeaders() {
        assertThat(request.getHeader("Host"), is("localhost:8080"));
    }

    @Test(expected = HeaderNotFoundException.class)
    public void readHeaders_err() {
        request.getHeader("colin");
    }

    @Test
    public void readBody() {
        assertThat(request.getBody(), is(""));
    }

    @Test
    public void readQueryParams() {
        assertThat(signRequest.getParam("userId"), is("colin"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void readQueryParams_err_not_exist() {
        assertThat(request.getParam("userId"), is("colin"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void readQueryParams_err() {
        signRequest.getParam("invalid");
    }
}
