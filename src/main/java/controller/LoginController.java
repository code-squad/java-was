package controller;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class LoginController extends AbstractController{
    private static final String baseUrl = "./webapp";
    @Override
    void doPost(HttpRequest request, HttpResponse response) {
        try {
            byte[] body = "".getBytes();
            System.out.println("!!!!" + request.getPath());

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
