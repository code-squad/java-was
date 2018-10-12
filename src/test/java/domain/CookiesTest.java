package domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CookiesTest {
    private Cookies cookies;

    @Before
    public void setUp() {
        cookies = new Cookies(new HashMap<String, String>());
    }

    @Test
    public void addCookie() {
        cookies.add("logined", "true");
        assertThat(cookies.toString() , is("logined=true"));
    }

    @Test
    public void getValue() {
        cookies.add("logined", "true");
        assertThat(cookies.get("logined"), is("true"));
    }

    @Test
    public void matchValue() {
        cookies.add("logined", "true");
        assertThat(cookies.matchValue("logined", "true"), is(true));
    }

    @Test
    public void matchValue_fail() {
        cookies.add("logined", "true");
        assertThat(cookies.matchValue("logined", "fail"), is(false));
    }
}
