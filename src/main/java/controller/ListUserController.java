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
        String header = request.getHeader("Cookie");
        if (isLogin(header)) {
            List<User> users = UserService.findAll();
            String usersHtml = HttpResponseUtils.generateUsersBody(users);

            response.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);
            response.forwardBody(usersHtml);
        }
        if (!isLogin(header)) {
            response.sendRedirect("/user/login.html");
        }
    }

    private boolean isLogin(String header) {
        Map<String, String> cookie = HttpRequestUtils.parseCookies(header);
        if (cookie.isEmpty())
            return false;
        return cookie.get("logined").equals("true");
    }
}
