package controller;

import domain.HttpRequest;
import domain.HttpResponse;
import domain.Url;
import service.UserService;

public class LoginController extends AbstractController {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        UserService userService = new UserService();

        if (userService.login(String.valueOf(request.getBody()))) {
            response.addCookie("logined", "true");
            response.sendRedirect(Url.HOME);
            return;
        }
        response.addCookie("logined", "false");
        response.sendRedirect(Url.LOGIN_FAIL);
    }
}
