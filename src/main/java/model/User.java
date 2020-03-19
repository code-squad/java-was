package model;

import java.util.Map;
import java.util.Objects;

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

  public User(Map<String, String> userParameterMap) {
    this.userId = userParameterMap.get("userId");
    this.password = userParameterMap.get("password");
    this.name = userParameterMap.get("name");
    this.email = userParameterMap.get("email");
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return Objects.equals(getUserId(), user.getUserId()) &&
           Objects.equals(getPassword(), user.getPassword()) &&
           Objects.equals(getName(), user.getName()) &&
           Objects.equals(getEmail(), user.getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUserId(), getPassword(), getName(), getEmail());
  }

  @Override
  public String toString() {
    return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
  }
}
