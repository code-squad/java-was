package codesquad.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CookieTest {

    @Test
    public void isEmptyTest() {
        Cookie cookie = new Cookie();
        assertThat(cookie.hasValue(), is(false));
    }

    @Test
    public void hasValue() {
        Cookie cookie = new Cookie();
        cookie.setAttribute("logined", true);
        assertThat(cookie.hasValue(), is(true));
    }
}