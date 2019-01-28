package security;

import model.User;
import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    /* key : JSESSIONID, Object(User.class) */
    private static Map<String, ClientSession> httpSessions = new HashMap<>();

    private String jSessionId;

    public HttpSession() {

    }

    public HttpSession(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public void addSession(Object object) {
        if(httpSessions.containsKey(this.jSessionId)) {
            httpSessions.put(this.jSessionId, httpSessions.get(this.jSessionId).registerSession(object));
        }
        httpSessions.put(this.jSessionId, new ClientSession().registerSession(object));
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

    public boolean isLoginUser() {
        if(!httpSessions.containsKey(this.jSessionId) || !httpSessions.get(this.jSessionId).hasSession(ClientSession.LOGIN_SESSION)) {
            return false;
        }
        return true;
    }

    public User obtainLoginUser() {
        return httpSessions.get(this.jSessionId).getLoginUser();
    }
}
