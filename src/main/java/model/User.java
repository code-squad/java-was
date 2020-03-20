package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static constants.CommonConstants.*;
import static constants.CommonConstants.UTF_8;

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

    public User(Map<String, String> params) throws UnsupportedEncodingException {
        this.userId = URLDecoder.decode(params.get(USER_ID), UTF_8);
        this.password = URLDecoder.decode(params.get(PASSWORD), UTF_8);
        this.name = URLDecoder.decode(params.get("name"), UTF_8);
        this.email = URLDecoder.decode(params.get("email"), UTF_8);
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
