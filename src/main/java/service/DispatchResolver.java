package service;

import controller.Controller;
import controller.Home;
import controller.StaticControl;
import dto.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DispatchResolver {
    static private Map<Integer, Controller> resolver = new HashMap<>();

    static {
        resolver.put(hashCode("static", "GET"), new StaticControl());
        resolver.put(hashCode("/index.html", "GET"), new Home());

    }

    private static int hashCode(String path, String method) {
        return Objects.hash(path, method);
    }

    public static Controller get(HttpRequest request) {
        if (isaStyle(request.url())){
            return resolver.get(hashCode("static", "GET"));
        }
        return resolver.get(hashCode(request.url(), request.method()));
    }

    private static boolean isaStyle(String url) {
        return url.endsWith("favicon.ico") || url.contains("/js/") || url.contains("/css/") || url.contains("/fonts/");
    }
}
