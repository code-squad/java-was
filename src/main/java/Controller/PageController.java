package Controller;

import model.User;

public class PageController {

  public void doWork(String[] tokens) {

    switch(tokens[0]) {
      case "GET":
        break;
      case "POST":
        if (tokens[1].startsWith("/user/create")) {
          User createdUser = UserController.create(tokens[1].substring(tokens[1].indexOf("?") + 1));
        }
        break;
      default:
        break;
    }


    if (tokens[1].startsWith("/user/create")) {
      User createdUser = UserController.create(tokens[1].substring(tokens[1].indexOf("?") + 1));
    }
  }
}
