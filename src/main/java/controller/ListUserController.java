package controller;

import model.User;
import service.UserService;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ListUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String logined = request.getHeader("Cookie");
        if (isLogin(logined)) {
            List<User> users = UserService.findAll();
            byte[] body = HttpResponseUtils.generateUsersBody(users);
            HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
            HttpResponseUtils.responseBody(response, body);
        }
        if (isLogin(logined)) {
            HttpResponseUtils.response302Header(response, "/user/login.html");
        }
        HttpResponseUtils.responseSend(response);
    }

    private boolean isLogin(String logined) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(logined);

        return true;
    }
}
