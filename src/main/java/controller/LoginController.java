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
        String path = request.getPath();
        if (path.endsWith("/login.html"))
            loginForm(request, response);

        if (path.endsWith("/login_failed.html"))
            loginFailedForm(request, response);
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

    private void loginForm(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(makeHtmlUrl(request.getPath()));
        HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }

    private void loginFailedForm(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(makeHtmlUrl(request.getPath()));
        HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }
}
