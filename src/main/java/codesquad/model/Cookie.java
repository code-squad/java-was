package codesquad.model;

import com.google.common.collect.Maps;

import java.util.Map;

public class Cookie {

    private Map<String, Object> cookieVal;

    public Cookie() {
        cookieVal = Maps.newHashMap();
    }

    public void setAttribute(String key, Object value) {
        cookieVal.put(key, value);
    }

    public Map<String, Object> getCookieVal() {
        return cookieVal;
    }

    public boolean hasValue() {
        return !cookieVal.isEmpty();
    }

    public String writeCookie() {
        StringBuilder sb = new StringBuilder();
        for (String key : cookieVal.keySet()) {
            sb.append(key + "=" + cookieVal.get(key) + ";");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "cookieVal=" + cookieVal +
                '}';
    }
}
