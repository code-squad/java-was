package webserver.controller;

import org.junit.Before;
import org.junit.Test;
import support.TestOutputStream;
import webserver.support.controller.HandlerMapper;
import webserver.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;

import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UserControllerTest {
    private Controller controller;
    private Request request;
    private Response response;

    @Before
    public void setUp() throws Exception {
        request = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("signUpRequest.txt").getFile())));
        response = new Response(new TestOutputStream());
        controller = HandlerMapper.mapHandler(request);
    }

    @Test
    public void create() {
        response = controller.process(request, response);
        assertThat(response.getStatus(), is(HttpStatus.FOUND));
        assertThat(response.getPath(), is("/index.html"));
    }
}