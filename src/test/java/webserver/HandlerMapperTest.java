package webserver;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;


public class HandlerMapperTest {
    private static final Logger log = LoggerFactory.getLogger(HandlerMapperTest.class);

    @Test
    public void map() {
        Controller controller = HandlerMapper.mapHandler("/user/create");
        assertTrue(controller.getClass().getName().toLowerCase().contains("user"));
    }
}