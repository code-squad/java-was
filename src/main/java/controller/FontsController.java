package controller;

import util.HttpResponseUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class FontsController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(request.getPath());
        HttpResponseUtils.response200Header(response, body.length, getFontsType(request.getPath()));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }
}
