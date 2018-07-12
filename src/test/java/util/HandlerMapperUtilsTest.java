package util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HandlerMapperUtilsTest {

    @Test
    public void parseControllerName() {
        assertThat(HandlerMapperUtils.parseControllerName("/user/1"), is("/user"));
    }

    @Test
    public void parseControllerName2() {
        assertThat(HandlerMapperUtils.parseControllerName("/user/create?userId=colin&password=1234&name=colin&email=colin@codesquad.kr"), is("/user"));
    }

    @Test
    public void parseControllerName3() {
        assertThat(HandlerMapperUtils.parseControllerName("/user/1/create"), is("/user"));
    }

    @Test
    public void parseControllerName4() {
        assertThat(HandlerMapperUtils.parseControllerName("/user"), is("/user"));
    }
}