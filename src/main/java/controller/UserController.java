package controller;

import exception.UnAuthenticationException;
import model.User;
import service.UserService;
import util.ObjectMaker;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import util.HttpResponseUtils;

import java.io.IOException;
import java.util.List;

public class UserController extends AbstractController {


    public static void loginForm(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(makeHtmlUrl(request.getPath()));
        HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }

    public static void loginForm_failed(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(makeHtmlUrl(request.getPath()));
        HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }


    public static void list(HttpRequest request, HttpResponse response) {
//        if (request.isLogined()) {
//            List<User> users = UserService.findAll();
//            byte[] body = HttpResponseUtils.generateUsersBody(users);
//            HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
//            HttpResponseUtils.responseBody(response, body);
//        }
//        if (!request.isLogined()) {
//            HttpResponseUtils.response302Header(response, "/user/login.html");
//        }
//        HttpResponseUtils.responseSend(response);
    }
}
