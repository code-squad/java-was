package webserver.request;

import exception.NotFoundRequestParameterException;
import org.junit.Before;
import org.junit.Test;
import util.RequestUtils;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RequestParameterTest {

    private RequestParameter parameter;
    private RequestParameter emptyParameter;

    @Before
    public void setUp() throws Exception {
        parameter = new RequestParameter(RequestUtils.splitQueryString("userId=colin&password=1234&name=colin&email=colin@codesquad.kr"));
        emptyParameter = new RequestParameter(new HashMap<>());
    }

    @Test
    public void isEmpty() {
        assertFalse(parameter.isEmpty());
    }

    @Test
    public void isEmpty_true() {
        assertTrue(emptyParameter.isEmpty());
    }

    @Test
    public void containsKey() {
        assertTrue(parameter.containsKey("userId"));
    }

    @Test
    public void containsKey_false() {
        assertFalse(parameter.containsKey("codesquad"));
    }

    @Test
    public void getValue() {
        assertThat(parameter.get("userId"), is("colin"));
        assertTrue(parameter.containsKey("userId"));
    }

    @Test(expected = NotFoundRequestParameterException.class)
    public void getValue_fail() {
        parameter.get("codesquad");
    }
}