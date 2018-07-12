package webserver.request;

import exception.NotFoundHeaderException;
import exception.NotFoundRequestParameterException;
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
    private Request signUpRequest;

    @Before
    public void setUp() throws Exception {
        request = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("request.txt").getFile())));
        signUpRequest = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("signUpRequest.txt").getFile())));
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

    @Test(expected = NotFoundHeaderException.class)
    public void readHeaders_err() {
        request.getHeader("colin");
    }

    @Test
    public void readBody() {
        assertThat(request.getBody(), is(""));
    }

    @Test
    public void readQueryParams() {
        assertThat(signUpRequest.getParam("userId"), is("colin"));
    }

    @Test(expected = NotFoundRequestParameterException.class)
    public void readQueryParams_err_not_exist() {
        assertThat(request.getParam("userId"), is("colin"));
    }

    @Test(expected = NotFoundRequestParameterException.class)
    public void readQueryParams_err() {
        signUpRequest.getParam("invalid");
    }
}
