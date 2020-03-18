package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Map<String, String> parameterMap) throws UnsupportedEncodingException {
        this.userId = URLDecoder.decode(parameterMap.get("userId"), "UTF-8");
        this.password = URLDecoder.decode(parameterMap.get("password"), "UTF-8");
        this.name = URLDecoder.decode(parameterMap.get("name"), "UTF-8");
        this.email = URLDecoder.decode(parameterMap.get("email"), "UTF-8");
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
