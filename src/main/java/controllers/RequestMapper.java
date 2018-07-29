package controllers;

import webserver.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {
    private static final Map<String, Class<? extends RequestProcessor>> MAPPINGS = new HashMap<>();

    {
        MAPPINGS.put("/user/create", UserCreationProcessor.class);
        MAPPINGS.put("/user/login.html", UserLoginPageProcessor.class);
        MAPPINGS.put("/user/login", UserLoginProcessor.class);
        MAPPINGS.put("/user/list", UserListProcessor.class);
    }

    public static RequestProcessor getProcessor(HttpRequest request) throws IllegalAccessException, InstantiationException {
        String endPoint = request.getPath();
        return MAPPINGS.get(endPoint).newInstance();
    }
}
