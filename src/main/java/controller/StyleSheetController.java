package controller;

import util.HttpResponseUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public class StyleSheetController extends AbstractController {
    public static void styleSheet(HttpRequest request, HttpResponse response) throws IOException {
        byte[] body = HttpResponseUtils.generateBody(request.getUri());
        HttpResponseUtils.response200Header(response, body.length, makeContentType(request.getAcceptType()));
        HttpResponseUtils.responseBody(response, body);
        HttpResponseUtils.responseSend(response);
    }
}
