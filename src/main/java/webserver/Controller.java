package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    public static void createUser(HttpRequest request, HttpResponse response) throws IOException {
        User user = new User(request.getParams("userId"), request.getParams("password"), request.getParams("name"), request.getParams("email"));
        DataBase.addUser(user);
        log.debug("User : {}", user);
        response.redirect("/index.html");
    }

    public static void login(HttpRequest request, HttpResponse response) throws IOException {
        User user = DataBase.findUserById(request.getParams("userId"));
        if (user == null || !user.matchPassword(request.getParams("password"))) {
            response.loginFailed("/user/login_failed.html");
            log.debug("Login failed");
            return;
        }
        log.debug("Login Success");
        response.redirectWithCookie("/index.html");
    }

    public static void getStaticFile(HttpRequest request, HttpResponse response) throws IOException {
        String accept = request.getHeader("Accept");
            log.debug("Stylesheet~! : {}", accept);
        if (accept != null && accept.contains("text/css")) {
            log.debug("Stylesheet! : {}", accept);
            response.getStylesheet(request.getUrl());
            return;
        }
        response.getResponse(request.getUrl());
    }
}
