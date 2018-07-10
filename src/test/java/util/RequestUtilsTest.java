package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUtils.Pair;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(RequestUtilsTest.class);

    @Test
    public void splitPathAndParams() {
        List<String> token = RequestUtils.splitPathAndParams("/user/create?userId=colin&password=1234&name=colin&email=colin@codesquad.kr");
        assertThat(token.size(), is(2));
    }

    @Test
    public void splitParams() {
        List<Pair> pairs = RequestUtils.splitParams("userId=colin&password=1234&name=colin&email=colin@codesquad.kr");
        assertThat(pairs.size(), is(4));
        for (Pair pair : pairs) {
            log.debug("param : {} - {}", pair.getKey(), pair.getValue());
        }
    }
}