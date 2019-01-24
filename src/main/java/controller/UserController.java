package controller;

import exception.UnAuthenticationException;
import model.User;
import service.UserService;
import util.ObjectMaker;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import util.HttpResponseUtils;

import java.io.IOException;

public class UserController extends AbstractController {
    public static void createForm(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(makeHtmlUrl(request.getUri()));
        HttpResponseUtils.response200Header(response.getResponse(), body.length);
        HttpResponseUtils.responseBody(response.getResponse(), body);
    }

    public static void create(HttpRequest request, HttpResponse response) {
        User newUser = ObjectMaker.makeNewUser(request.getRequestBody());
        UserService.addUser(newUser);
        HttpResponseUtils.response302Header(response.getResponse());
        HttpResponseUtils.responseBody(response.getResponse(), new byte[] {});
    }

    public static void loginForm(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(makeHtmlUrl(request.getUri()));
        HttpResponseUtils.response200Header(response.getResponse(), body.length);
        HttpResponseUtils.responseBody(response.getResponse(), body);
    }

    public static void login(HttpRequest request, HttpResponse response) {
        User loginUser = ObjectMaker.makeNewUser(request.getRequestBody());
        try {
            UserService.login(loginUser);

        } catch (UnAuthenticationException e) {

        }

    }
}
