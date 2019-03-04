package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public class CreateUserController extends AbstractController {
    private static final String URL_INDEX = "/index.html";

    @Override
    void doPost(HttpRequest request, HttpResponse response) {
        byte[] body = "".getBytes();
        try {
            response.sendRedirect(URL_INDEX, false);
            response.forward(request.getPath());
            response.responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
