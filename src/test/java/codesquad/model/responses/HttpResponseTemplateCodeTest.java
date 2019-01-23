package codesquad.model.responses;

import org.junit.Test;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpResponseTemplateCodeTest {
    private static final Logger log = getLogger(HttpResponseTemplateCodeTest.class);

    @Test
    public void name() {
        log.debug(String.valueOf(ResponseCode.FOUND.getCode()));
    }
}