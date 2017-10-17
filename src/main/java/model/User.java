package model;

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
    
    public static User createNewUser(Map <String, String> userMap) {
    		return new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
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
    
    public boolean isLoginInfoCorrect(String password) {
    		return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
