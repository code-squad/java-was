package Controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class WelcomeController {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public void doWork(String[] tokens) {

    if (tokens[1].startsWith("/user/create")) {
      User createdUser = UserController.create(tokens[1].substring(tokens[1].indexOf("?") + 1));
    }
  }
}
