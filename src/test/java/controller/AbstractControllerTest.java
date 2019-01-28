package controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class AbstractControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    @Test
    public void endWithTest() {
        String v = "/user/loginForm.html";

        assertTrue(v.endsWith("loginForm.html"));
    }

    @Test
    public void containsTest() {
        String v = "/user/login_failed.html";
        String v2 = "/user/login.html";

        logger.debug("contains : {}", v.contains("login"));

        assertTrue(v.contains("login") && v.contains(".html"));
    }
}