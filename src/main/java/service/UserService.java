package service;

import model.User;
import util.HttpRequestUtils;

import java.util.Map;

public class UserService {

    public User createUser(String body) {
        Map<String, String> userData = HttpRequestUtils.parseQueryString(body);
        return new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
    }
}
