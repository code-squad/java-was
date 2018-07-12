package webserver.support.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HandlerMapperUtils;
import webserver.controller.Controller;

import static org.junit.Assert.*;

public class HandlerMapperTest {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapperTest.class);

    @Test
    public void mapHandler_view() {
        Controller controller = HandlerMapper.mapHandler(HandlerMapperUtils.parseControllerName("/index.html"));
        assertTrue(controller.getClass().getName().contains("ViewController"));
    }
}