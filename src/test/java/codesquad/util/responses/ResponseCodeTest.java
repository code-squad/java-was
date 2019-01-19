package codesquad.util.responses;

import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

public class ResponseCodeTest {
    private static final Logger log = getLogger(ResponseCodeTest.class);

    @Test
    public void name() {
        log.debug(String.valueOf(ResponseCode.FOUND.getCode()));
    }
}