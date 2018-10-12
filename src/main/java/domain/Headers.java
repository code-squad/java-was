package domain;

import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;

import static util.HttpRequestUtils.Pair;

public class Headers {
    private List<Pair> pairs = new ArrayList<>();
    private Cookies cookies;

    public String getValue(String key) {
        // TODO 빈 객체를 리턴하던가, Optional객체를 리턴하던가
        return pairs.stream().filter(pair -> pair.getKey().equals(key)).findAny().map(Pair::getValue).orElse("");
    }

    public void add(Pair pair) {
        if (pair.getKey().equals("Cookie")) {
            cookies = new Cookies(HttpRequestUtils.parseCookies(pair.getValue()));
            return;
        }
        pairs.add(pair);
    }

    public void addCookie(String key, String value) {
        cookies.add(key, value);
    }

    public Cookies getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return "Headers{" +
                "pairs=" + pairs +
                '}';
    }
}
