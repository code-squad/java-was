package Controller;

import model.User;

public class WelcomeController {

  public void doWork(String[] tokens) {
    if (tokens[1].startsWith("/user/create")) {
      User createdUser = UserController.create(tokens[1].substring(tokens[1].indexOf("?") + 1));
    }
  }
}
