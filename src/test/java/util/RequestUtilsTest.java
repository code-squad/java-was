package util;

import org.junit.Test;
import webserver.HttpMethod;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RequestUtilsTest {

    @Test
    public void splitRequestLine() {
        List<String> token = RequestUtils.splitRequestLine("GET /index.html HTTP/1.1");
        assertThat(token.size(), is(3));
        assertThat(token.get(0), is(HttpMethod.GET.name()));
    }

    @Test
    public void splitPathAndParams() {
        List<String> token = RequestUtils.splitPathAndParams("/user/create?userId=colin&password=1234&name=colin&email=colin@codesquad.kr");
        assertThat(token.size(), is(2));
    }

    @Test
    public void splitQueryString() {
        Map<String, String> queryString = RequestUtils.splitQueryString("userId=colin&password=1234&name=colin&email=colin@codesquad.kr");
        assertThat(queryString.size(), is(4));
        assertTrue(queryString.containsKey("userId"));
    }
}