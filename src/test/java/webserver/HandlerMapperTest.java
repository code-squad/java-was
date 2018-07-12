package webserver;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.RequestMapping;
import webserver.controller.Controller;
import webserver.support.controller.HandlerMapper;

import static org.junit.Assert.assertTrue;


public class HandlerMapperTest {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapperTest.class);

    @Test
    public void mapping() {
        Controller controller = HandlerMapper.mapHandler("/user/create");
        assertTrue(controller.getClass().getAnnotation(RequestMapping.class).value().contains("user"));
    }
}