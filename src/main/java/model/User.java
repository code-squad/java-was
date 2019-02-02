package model;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public static User of(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }

    public static User ofEmpty() {
        return new User();
    }

    private User() {

    }

    private User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    public boolean isCorrect(String password) {
        return this.password.equals(password);
    }

    public boolean isEmpty() {
        return this.userId == null;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", email=" + email + "]";
    }
}
