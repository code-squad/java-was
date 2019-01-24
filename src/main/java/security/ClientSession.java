package security;

import java.util.HashMap;
import java.util.Map;

public class ClientSession {
    public static final String LOGIN_SESSION = "loginedUser";

    private Map<String, Object> session;

    public ClientSession() {
        session = new HashMap<>();
    }

    public ClientSession registerSession(Object object) {
        session.put(LOGIN_SESSION, object);
        return this;
    }

    public boolean hasSession(String sessionName) {
        return session.containsKey(sessionName);
    }
}
