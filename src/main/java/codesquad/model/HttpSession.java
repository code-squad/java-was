package codesquad.model;

import com.google.common.collect.Maps;

import java.util.Map;

public class HttpSession {

    private Map<String, Object> newCookie;

    public HttpSession() {
        newCookie = Maps.newHashMap();
    }

    public void setAttribute(String key, Object value) {
        newCookie.put(key, value);
    }

    public Object getAttribute(String key) { return newCookie.get(key); }

    public boolean hasValue() {
        return !newCookie.isEmpty();
    }

    public void putCookie(Map<String, String> cookie) {
        for (String key : newCookie.keySet()) {
            cookie.put(key, newCookie.get(key).toString());
        }
    }

    public Object addCookie(Header header) {
        header.putCookie(newCookie);
        return this;
    }

    @Override
    public String toString() {
        return "HttpSession{" +
                "newCookie=" + newCookie +
                '}';
    }
}
