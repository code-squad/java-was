package controller;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

public class ListUserController extends AbstractController {
    private static final String baseUrl = "./webapp";

    @Override
    void doGet(HttpRequest request, HttpResponse response) {
        try {
            String path = request.getPath();
            if (isLogin(request.getHeader("Cookie"))) {
                StringBuilder sb = new StringBuilder();
                Collection<User> list = DataBase.findAll();
                for (User user : list) {
                    sb.append(user.getUserId());
                    sb.append(user.getName());
                    sb.append(user.getEmail());
                }
            } else {
                path = "/user/login.html";
            }

            byte[] body = Files.readAllBytes(new File(baseUrl + path).toPath());
            response.response200Header(body.length);
            response.forward(request.getPath());
            response.responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLogin(String cookie) {
        System.out.println(cookie);
        if (cookie.equals("logined=false")) {
            return false;
        }
        return true;
    }
}
