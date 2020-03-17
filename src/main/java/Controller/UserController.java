package Controller;

import model.User;
import util.HttpRequestUtils;

import java.util.Map;

public class UserController {

  public static User create(String uriString) {
    Map<String, String> parameters = HttpRequestUtils.parseUriString(uriString);
    User user = new User(parameters);

    return user;
  }
}
