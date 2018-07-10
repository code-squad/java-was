package util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HandlerMapperUtilsTest {

    @Test
    public void parseControllerName() {
        assertThat(HandlerMapperUtils.parseControllerName("/users/1"), is("users"));
    }
}