package Controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;

import java.util.Arrays;
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
        } else if (requestUrl.equals("/user/login_failed.html")) {
          responseBodyUrl = "/user/login_failed.html";
          statusCode = "200";
          message = "OK";
        } else if (requestUrl.equals("/user/list")) {
          boolean de = false;
          log.debug("### requestHeader : {}", requestHeader);
          log.debug("### requestHeader : {}", requestHeader.get("Cookie").getValue());
//          ### requestHeader : null;logined=true;null
          String cookie = requestHeader.get("Cookie").getValue().replaceAll(" ","");
//          ### requestHeader : null;logined=true;null
          boolean delemeter = Arrays.stream(cookie.split(";")).anyMatch(token -> token.startsWith("logined=true"));
          if (delemeter) {
            responseBodyUrl = "/user/list.html";
            statusCode = "200";
            message = "OK";
          } else {
            responseBodyUrl = "/user/list.html";
            statusCode = "302";
            location = "http://localhost:8080/user/login.html";
            message = "Found";
          }
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
          Map<String, String> parsedRequestBody = HttpRequestUtils.parseRequestBody(requestBody);
          User loginUser =
              new User(parsedRequestBody.get("userId"), parsedRequestBody.get("password"), "", "");
          User findUser = DataBase.findUserById(parsedRequestBody.get("userId"));
          log.debug("### login check : {}", loginUser.getPassword().equals(findUser.getPassword()));

          if (loginUser.getPassword().equals(findUser.getPassword())) {
            responseBodyUrl = "/index.html";
            statusCode = "302";
            location = "http://localhost:8080/index.html";
            message = "Found";
            response.put("Set-Cookie", "logined=true; Path=/");
          } else {
            responseBodyUrl = "/user/login_failed.html";
            statusCode = "302";
            location = "http://localhost:8080/user/login_failed.html";
            message = "Found";
            response.put("Set-Cookie", "logined=false; Path=/");
          }
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
