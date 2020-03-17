package controller;

import Controller.UserController;
import model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

  @Test
  public void create() {
    String uriString = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

    User expectedUser = new User("javajigi", "password", "%EB%B0%95%EC%9E%AC%EC%84%B1", "javajigi%40slipp.net");
    assertEquals(expectedUser, UserController.create(uriString));
  }
}
