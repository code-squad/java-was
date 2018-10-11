package domain;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Cookies {
    Map<String, String> cookies;

    public Cookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void add(String key, String value) {
        if (cookies.get(key) != null) {
            cookies.replace(key, value);
            return;
        }
        cookies.put(key, value);
    }

    public String get(String key) {
        return cookies.get(key);
    }

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

    @Override
    public String toString() {
        return cookies.entrySet()
                .stream()
                .map(e -> e.getKey()+"="+e.getValue())
                .collect(joining(";"));
    }

    public boolean matchValue(String key, String value) {
        if (cookies.isEmpty()) {
            return false;
        }
        return cookies.get(key).equals(value);
    }
}
