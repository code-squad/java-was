package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainController extends AbstractController {
    private static final String baseUrl = "./webapp";

    @Override
    void doGet(HttpRequest request, HttpResponse response) {
        try {
            byte[] body = Files.readAllBytes(new File(baseUrl + request.getPath()).toPath());
            response.response200Header(body.length);
            response.forward(request.getPath());
            response.responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
