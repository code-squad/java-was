package controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import util.HttpResponseUtils;

import java.io.IOException;

public class IndexController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader(CONTENT_TYPE, HTML_CONTENT_TYPE);
        response.forward(request.getPath());
    }
}
