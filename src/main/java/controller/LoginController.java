package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public class LoginController extends AbstractController {
    @Override
    void doPost(HttpRequest request, HttpResponse response) {
        try {
            byte[] body = "".getBytes();

            String path = "/user/login_failed.html";
            if (request.isLogin()) {
                path = "/index.html";
            }
            response.sendRedirect(path, request.isLogin());
            response.forward(request.getPath());
            response.responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
