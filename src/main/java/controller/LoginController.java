package controller;

import exception.UnAuthenticationException;
import model.User;
import service.UserService;
import util.ObjectMaker;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class LoginController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);
        response.forward(request.getPath());
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User loginUser = ObjectMaker.makeNewUser(request);
        try {
            UserService.login(loginUser);
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
        } catch (UnAuthenticationException e) {
            response.addHeader("Set-Cookie", "logined=false");
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
