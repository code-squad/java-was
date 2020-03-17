package Controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class PageController {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public String doWork(String requestLine) {
    log.debug("### dowork");
    String[] tokens = requestLine.split(" ");

    switch (tokens[0]) {
      case "GET":
        log.debug("### dowork : GET");
        if (tokens[1].equals("/user/form.html")) {
          return "/user/form.html";
        }
        break;
      case "POST":
        if (tokens[1].startsWith("/user/create")) {
          User createdUser = UserController.create(tokens[1].substring(tokens[1].indexOf("?") + 1));
        }
        break;
      default:
        break;
    }


    //    if (tokens[1].startsWith("/user/create")) {
    //      User createdUser = UserController.create(tokens[1].substring(tokens[1].indexOf("?") + 1));
    //    }

    return "";
  }
}
