package controller;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class AbstractControllerTest {

    @Test
    public void makeHtmlUrl() {
        String uri1 = "index";
        String uri2 = "index.html";

        String v1 = AbstractController.makeHtmlUrl(uri1);
        String v2 = AbstractController.makeHtmlUrl(uri2);

        assertThat(v1, is(v2));
    }

    @Test
    public void endWithTest() {
        String v = "/user/loginForm.html";

        assertTrue(v.endsWith("loginForm"));
    }
}