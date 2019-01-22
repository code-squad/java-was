package security;

import java.util.HashMap;
import java.util.Map;

public class ClientSession {

    private Map<String, Object> session;

    public ClientSession() {
        session = new HashMap<>();
    }

    public ClientSession registerSession(String sessionName, Object object) {
        session.put(sessionName, object);
        return this;
    }

    public boolean hasSession(String sessionName) {
        return session.containsKey(sessionName);
    }
}
