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

    @Before
    public void setUp() throws Exception {
        request = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("request.txt").getFile())));
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
}
