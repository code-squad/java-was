package controller;

import util.HttpRequestUtils;
import vo.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.util.List;

public abstract class AbstractController implements Controller {
    private static final String HTML_EXTENSION = ".html";
    private static final String CSS_DIRECTIVE = ",*/*;q=0.1";
    private static final String HTML_DIRECTIVE = ";charset=utf-8";

    static String makeHtmlUrl(String uri) {
        if (uri.endsWith(HTML_EXTENSION))
            return uri;
        return uri + HTML_EXTENSION;
    }

    static String makeContentType(String acceptType) {
        List<String> accepts = HttpRequestUtils.parseAccepts(acceptType);
        if (accepts.get(0).endsWith("/css"))
            return acceptType + CSS_DIRECTIVE;
        return acceptType + HTML_DIRECTIVE;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if (request.getMethod().equals(HttpMethod.GET))
            doGet(request, response);
        if (request.getMethod().equals(HttpMethod.POST))
            doPost(request, response);
    }

    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
    }

    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
    }
}
