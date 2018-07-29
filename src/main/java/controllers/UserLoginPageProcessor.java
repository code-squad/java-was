package controllers;

import exception.MethodNotAllowedException;
import webserver.*;

public class UserLoginPageProcessor implements RequestProcessor {

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        if (!request.isMethod(HttpMethod.GET)) {
            throw new MethodNotAllowedException("Only GET method is allowed.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8");
        return new HttpResponse(HttpStatus.OK, headers, Resource.of("/login.html"));
    }
}
