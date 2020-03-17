package Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;
import webserver.WebServer;

import java.util.Map;

public class PageController {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public String doWork(Map<String, HttpRequestUtils.Pair> requestLine, String requestBody) {
    log.debug("### dowork");

    String method = requestLine.get("method").getValue();
    String requestUrl = requestLine.get("requestUrl").getValue();
    String protocol = requestLine.get("protocol").getValue();

    switch (method) {
      case "GET":
        log.debug("### dowork : GET");
        if (requestUrl.equals("/user/form.html")) {
          return "/user/form.html";
        }
        break;
      case "POST":
        if (requestUrl.equals("/user/create")) {

          UserController.create(requestBody);
          log.debug("### userDB : {}", WebServer.userDB);
          return "/index.html";
        }
        break;
      default:
        break;
    }

    return "";
  }
}
