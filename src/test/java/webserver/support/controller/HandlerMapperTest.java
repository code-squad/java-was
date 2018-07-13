package webserver.support.controller;

import org.junit.Before;
import org.junit.Test;
import webserver.controller.Controller;
import webserver.request.Request;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertTrue;

public class HandlerMapperTest {
    private Request request1;
    private Request request2;
    private Request request3;

    @Before
    public void setUp() throws Exception {
        request1 = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("resourceRequest.txt").getFile())));
        request2 = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("secondResourceRequest.txt").getFile())));
        request3 = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("signUpRequest.txt").getFile())));
    }

    @Test
    public void mapHandler_view() {
        Controller controller = HandlerMapper.mapHandler(request1);
        assertTrue(controller.getClass().getName().contains("ViewController"));
    }

    @Test
    public void mapHandler_view_second() {
        Controller controller = HandlerMapper.mapHandler(request2);
        assertTrue(controller.getClass().getName().contains("ViewController"));
    }

    @Test
    public void mapHandler_user() {
        Controller controller = HandlerMapper.mapHandler(request3);
        assertTrue(controller.getClass().getName().contains("UserController"));
    }
}