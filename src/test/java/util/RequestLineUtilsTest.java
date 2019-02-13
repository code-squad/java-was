package util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestLineUtilsTest {

    @Test
    public void requestLineGetPath() {
        String input = "GET /index.html HTTP/1.1";
        String result = RequestLineUtils.getPath(input);
        assertThat(result).isEqualTo("/index.html");
    }
}