package controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class JavaScriptController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader(CONTENT_TYPE, JS_CONTENT_TYPE);
        response.forward(request.getPath());
    }
}
