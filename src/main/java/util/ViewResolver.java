package util;

import model.URLInfo;

import java.util.HashMap;
import java.util.Map;

public class ViewResolver {
    private static Map<URLInfo, String> viewMappingResolver = new HashMap<>();

    static {
        viewMappingResolver.put(new URLInfo("/users/create", "GET"), "././webapp/index.html");
        viewMappingResolver.put(new URLInfo("/index.html", "GET"), "./webapp/index.html");
    }

    public static String obtainReturnView(URLInfo urlInfo) {
        return viewMappingResolver.get(urlInfo);
    }
}
