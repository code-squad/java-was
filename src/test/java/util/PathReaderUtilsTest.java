package util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathReaderUtilsTest {
    @Test
    public void extractURL_present_path() {
        assertThat(PathReaderUtils.extractURL("GET /index.html HTTP/1.1").get()).isEqualTo("/index.html");
    }

    @Test
    public void extractURL_empty() {
        assertThat(PathReaderUtils.extractURL("GET  HTTP/1.1").isPresent()).isFalse();
    }
}