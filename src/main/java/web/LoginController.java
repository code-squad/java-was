package web;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public class LoginController extends AbstractController{

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    public void doPost(HttpRequest request, HttpResponse response){
        User user = DataBase.findUserById(request.getParams("userId"));
        if (user == null || !user.matchPassword(request.getParams("password"))) {
            response.addHeader("Set-Cookie", "logined=false; Path=/");
            response.sendRedirect("/user/login_failed.html");
            log.debug("Login failed");
            return;
        }
        log.debug("Login Success");
        response.addHeader("Set-Cookie", "logined=true; Path=/");
        response.sendRedirect("/index.html");
    }

    public void doGet(HttpRequest request, HttpResponse response){}
}
