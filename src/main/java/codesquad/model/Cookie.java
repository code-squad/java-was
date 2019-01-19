package codesquad.model;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    Map<String, Object> cookieVal = new HashMap<>();

    public void setAttribute(String key, Object value) {
        cookieVal.put(key, value);
    }

    public Map<String, Object> getCookieVal() {
        return cookieVal;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "cookieVal=" + cookieVal +
                '}';
    }
}
