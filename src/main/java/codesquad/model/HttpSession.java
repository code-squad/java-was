package codesquad.model;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpSession {
    private static final Logger log = getLogger(HttpSession.class);
    public static final String COOKIE_KEY = "SID";
    private static Map<UUID, Map<String, Object>> sessionRepository = new HashMap<>();

    private UUID sessionID;

    private Map<String, Object> sessionValues;

    public HttpSession() {
    }

    private HttpSession(UUID sessionId) {
        this.sessionID = sessionId;
        sessionValues = sessionRepository.getOrDefault(sessionId, Maps.newHashMap());
    }

    public static HttpSession of(String maySessionId) {
        if (Strings.isNullOrEmpty(maySessionId)) return new HttpSession(UUID.randomUUID());
        return new HttpSession(UUID.fromString(maySessionId));
    }

    public void setAttribute(String key, Object value) {
        sessionValues.put(key, value);
        sessionRepository.put(sessionID, sessionValues);
    }

    public Object getAttribute(String key) {
        return sessionValues.get(key);
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public void putCookie(Map<String, String> cookie) {
        cookie.put(COOKIE_KEY, sessionID.toString());
    }

    @Override
    public String toString() {
        return "HttpSession[sessionID=" + sessionID + ", sessionValues=" + sessionValues + ']';
    }
}
