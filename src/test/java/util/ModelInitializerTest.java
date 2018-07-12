package util;

import model.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.RequestParameter;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ModelInitializerTest {
    private static final Logger log = LoggerFactory.getLogger(ModelInitializerTest.class);
    private RequestParameter parameter;

    @Before
    public void setUp() throws Exception {
        parameter = new RequestParameter(RequestUtils.splitQueryString("userId=colin&password=1234&name=colin&email=colin@codesquad.kr"));
    }

    @Test
    public void userInit() {
        Optional<User> maybeUser = ModelInitializer.init(parameter, User.class);
        assertTrue(maybeUser.isPresent());

        User user = maybeUser.get();
        assertEquals("colin", user.getUserId());
        log.debug("user : {}", user.toString());
    }
}