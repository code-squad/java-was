package controller;

import exception.UnAuthenticationException;
import model.User;
import service.UserService;
import util.HttpResponseUtils;
import util.ObjectMaker;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class LoginController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        super.doGet(request, response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User loginUser = ObjectMaker.makeNewUser(request);
        try {
            UserService.login(loginUser);
            HttpResponseUtils.response302Header(response, "/");
            HttpResponseUtils.responseCookieHeader(response, true);
        } catch (UnAuthenticationException e) {
            HttpResponseUtils.response302Header(response, "/user/login_failed.html");
            HttpResponseUtils.responseCookieHeader(response, false);
        } finally {
            HttpResponseUtils.responseSend(response);
        }
    }
}
