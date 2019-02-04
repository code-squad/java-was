package service;

import controller.Controller;
import dto.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DispatchResolver {
    static private Map<Integer, Controller> resolver = new HashMap<>();

    static {
        resolver.put(hashCode("/index.html", "GET"), null);
    }

    private static int hashCode(String path, String method) {
        return Objects.hash(path, method);
    }

    public static Controller get(HttpRequest request) {
        return resolver.get(hashCode(request.url(), request.method()));
    }
}
