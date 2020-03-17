package Controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;
import webserver.WebServer;

import java.util.Map;

public class UserController {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public static User create(String uriString) {
    Map<String, String> parameters = HttpRequestUtils.parseUriString(uriString);
    log.debug("### parameters : {}", parameters);
    User user = new User(parameters);
    WebServer.userDB.put(WebServer.userDB.size() + 1, user);

    return user;
  }
}
