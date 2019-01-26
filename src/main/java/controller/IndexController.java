package controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import util.HttpResponseUtils;

import java.io.IOException;

public class IndexController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        String uri = "/index.html";
        byte[] body = HttpResponseUtils.generateBody(uri);
        HttpResponseUtils.response200Header(response, body.length, HTML_CONTENT_TYPE);
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }
}
