package Controller;

import db.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class PageController {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  public Map<String, String> doWork(Map<String, HttpRequestUtils.Pair> requestLine,
                                    Map<String, HttpRequestUtils.Pair> requestHeader, String requestBody)
  {
    Map<String, String> response = new HashMap<>();
    log.debug("### dowork");

    String responseBodyUrl = "";
    String statusCode = "";
    String location = "";
    String message = "";

    String method = requestLine.get("method").getValue();
    String requestUrl = requestLine.get("requestUrl").getValue();
    String protocol = requestLine.get("protocol").getValue();

    switch (method) {
      case "GET":
        log.debug("### dowork : GET");
        if (requestUrl.equals("/user/form.html")) {
          responseBodyUrl = "/user/form.html";
          statusCode = "200";
          message = "OK";
        } else if (requestUrl.equals("/index.html")) {
          responseBodyUrl = "/index.html";
          statusCode = "200";
          message = "OK";
        } else if (requestUrl.equals("/user/login.html")) {
          responseBodyUrl = "/user/login.html";
          statusCode = "200";
          message = "OK";
        }
        break;
      case "POST":
        if (requestUrl.equals("/user/create")) {
          UserController.create(requestBody);
          log.debug("### DataBase : {}", DataBase.findAll());
          log.debug("### requestBody : {}", requestBody);
          responseBodyUrl = "/index.html";
          statusCode = "302";
          location = "http://localhost:8080/index.html";
          message = "Found";
        }
        if (requestUrl.equals("/user/login")) {
          log.debug("### requestBody : {}", requestBody);
          //
          //          UserController.create(requestBody);
          //          //        log.debug("### userDB : {}", WebServer.userDB);
          //          responseBodyUrl = "/index.html";
          //          statusCode = "302";
          //          location = "http://localhost:8080/index.html";
          //          message = "Found";
        }
        break;
      default:
        break;
    }

    response.put("responseBodyUrl", responseBodyUrl);
    response.put("statusCode", statusCode);
    response.put("location", location);
    response.put("message", message);

    return response;
  }
}
