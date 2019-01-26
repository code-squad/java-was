package controller;

import util.HttpResponseUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class JavaScriptController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(request.getPath());
        HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getHeader("Accept")));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }
}
