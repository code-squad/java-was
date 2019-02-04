package service;

import controller.*;
import dto.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DispatchResolver {
    static private Map<Integer, Controller> resolver = new HashMap<>();

    static {
        resolver.put(hashCode("static", "GET"), new StyleSheet());
        resolver.put(hashCode("/index.html", "GET"), new Forward());
        resolver.put(hashCode("/user/form.html", "GET"), new Forward());
        resolver.put(hashCode("/user/login.html", "GET"), new Forward());
        resolver.put(hashCode("/user/create", "POST"), new UserCreate());
        resolver.put(hashCode("/user/login", "POST"), new UserLogin());
        resolver.put(hashCode("/user/list.html", "GET"), new UserList());

    }

    public static Controller get(HttpRequest request) {
        if (isStyleSheet(request.url())) {
            return resolver.get(hashCode("static", "GET"));
        }
        return resolver.get(hashCode(request.url(), request.method()));
    }

    private static boolean isStyleSheet(String url) {
        return url.endsWith("favicon.ico") || url.contains("/js/") || url.contains("/css/") || url.contains("/fonts/");
    }

    private static int hashCode(String path, String method) {
        return Objects.hash(path, method);
    }
}
