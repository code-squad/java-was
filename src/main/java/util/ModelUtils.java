package util;

import model.User;

import java.util.Map;

public class ModelUtils {
    public static User createUser(String requestLine) {
        String queryString = requestLine.split(" ")[1];
        Map<String, String> userData = HttpRequestUtils.parseQueryString(queryString.split("\\?")[1]);
        return new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
    }
}
