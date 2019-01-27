package controller;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class StyleSheetController extends AbstractController {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        response.addHeader(CONTENT_TYPE, CSS_COTENT_TYPE);
        response.forward(request.getPath());
    }
}
