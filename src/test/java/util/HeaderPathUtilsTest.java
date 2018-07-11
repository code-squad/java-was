package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HeaderPathUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(HeaderPathUtilsTest.class);

    @Test
    public void name() {
        String line = "GET /index.html HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);
        assertThat(path, is("/index.html"));

        log.debug("path : {}", path);
    }
}
