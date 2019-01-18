package util;

import model.RequestHeader;

import java.util.HashMap;
import java.util.Map;

public class ViewResolver {
    private static Map<RequestHeader, String> viewMappingResolver = new HashMap<>();

    static {
        viewMappingResolver.put(new RequestHeader("/users/create", "GET", null), "././webapp/user/form.html");
        viewMappingResolver.put(new RequestHeader("/index.html", "GET", null), "./webapp/index.html");
        viewMappingResolver.put(new RequestHeader("/users/create", "POST", null), "././webapp/index.html");
    }

    public static String obtainReturnView(RequestHeader urlInfo) {
        return viewMappingResolver.get(urlInfo);
    }
}
