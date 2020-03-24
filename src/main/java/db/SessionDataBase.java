package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Collection;
import java.util.Map;

public class SessionDataBase {
    private static Map<String, User> sessions = Maps.newHashMap();

    public static void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    public static User getSessionedUser(String sessionId) {
        return sessions.get(sessionId);
    }

    public static boolean isSessionIdExist(String sessionId) {
        return sessions.containsKey(sessionId);
    }
}
