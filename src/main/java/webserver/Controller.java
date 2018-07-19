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
        response.sendRedirect("/index.html");
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

    public static void forward(HttpRequest request, HttpResponse response) throws IOException {
        String accept = request.getHeader("Accept");
        log.debug("Stylesheet! : {}", accept);
        if (accept != null && accept.contains("text/css")) {
            response.forward(request.getUrl(), ContentType.CSS);
            return;
        }
        response.forward(request.getUrl(), ContentType.HTML);
    }


    public static void showUser(HttpRequest request, HttpResponse response) throws Exception {
        String cookie = request.getHeader("Cookie");
        log.debug("Cookies : {}", cookie);
        if (!cookie.contains("logined=true")) {
            response.sendRedirect("/login.html");
            return;
        }
        ModelAndView modelAndView = new ModelAndView("/user/list.html");
        modelAndView.setAttribute("user", DataBase.findAll());
        response.modelAndViewResponse(modelAndView);
    }
}
