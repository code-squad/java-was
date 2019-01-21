package codesquad.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HttpSessionTest {

    @Test
    public void isEmptyTest() {
        HttpSession httpSession = new HttpSession();
        assertThat(httpSession.hasValue(), is(false));
    }

    @Test
    public void hasValue() {
        HttpSession httpSession = new HttpSession();
        httpSession.setAttribute("logined", true);
        assertThat(httpSession.hasValue(), is(true));
    }
}