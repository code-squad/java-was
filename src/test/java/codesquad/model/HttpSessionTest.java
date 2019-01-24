package codesquad.model;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class HttpSessionTest {
    private static final Logger log = getLogger(HttpSessionTest.class);

    @Test
    public void name() {
        Map<String, Map<String, Object>> sessionRepository = new HashMap<>();
        Map<String, Object> sessionValues = sessionRepository.getOrDefault(null, Maps.newHashMap());
        log.debug(sessionValues.toString());
    }

    @Test
    public void UUID_테스트() {
        UUID test = UUID.randomUUID();
        String testText = test.toString();
        log.debug("UUID String값 : {}", testText);
        assertThat(test.equals(UUID.fromString(testText)), is(true));
    }

    @Test
    public void UUID_생성() {
        HttpSession session = HttpSession.of(null);
        log.debug("SessionId : {}", session.getSessionID());
    }
}