package controller;

import model.User;
import service.UserService;
import util.HttpResponseUtils;
import util.ObjectMaker;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class CreateUserController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(request.getPath());
        HttpResponseUtils.response200Header(response, body.length, HTML_CONTENT_TYPE);
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        User newUser = ObjectMaker.makeNewUser(request);
        UserService.addUser(newUser);
        HttpResponseUtils.response302Header(response, "/index.html");
        HttpResponseUtils.responseBody(response, new byte[]{});
        HttpResponseUtils.responseSend(response);
    }
}
