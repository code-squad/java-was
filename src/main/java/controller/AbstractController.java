package controller;

import vo.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;

public abstract class AbstractController implements Controller {
    static final String CONTENT_TYPE = "Content-Type";
    static final String CSS_COTENT_TYPE = "text/css,*/*;q=0.1";
    static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
    static final String JS_CONTENT_TYPE = "text/javascript;charset=UTF-8";
    static final String WOFF_CONTENT_TYPE = "application/font-woff";
    static final String TTF_CONTENT_TYPE = "application/font-ttf";

    String getFontsType(String path) {
        if (path.endsWith(".woff"))
            return WOFF_CONTENT_TYPE;
        return TTF_CONTENT_TYPE;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (request.matchMethod(HttpMethod.GET))
            doGet(request, response);
        if (request.matchMethod(HttpMethod.POST))
            doPost(request, response);
    }

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
    }

    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
    }
}
