package Controller;

import util.HttpRequestUtils;

import java.util.Map;
import java.util.Set;

public class UserController {

  public String create(String uriString) {
    Map<String, String> parameters = HttpRequestUtils.parseUriString(uriString);

    String[] userInfo = new String[4];

    Set<String> keys = parameters.keySet();
    parameters.get(key);

    for (String key : parameters.keySet()) {
      parameters.get(key);
    }
  }

}
