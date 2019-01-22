package security;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    /* key : JSESSIONID, Obkect(User.class) */
    private static Map<String, ClientSession> httpSessions = new HashMap<>();

    public static void addSession(String jsessionID, ClientSession clientSession) {
        httpSessions.put(jsessionID, clientSession);
    }

    public static void removeSession(String jssesionID) {
        httpSessions.remove(jssesionID);
    }

    public static boolean isSession(String key) {
        return httpSessions.containsKey(key);
    }

    public static ClientSession getSession(String jsessionID) {
        return httpSessions.get(jsessionID);
    }
}
