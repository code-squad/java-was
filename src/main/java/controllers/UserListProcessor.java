package controllers;

import exception.MethodNotAllowedException;
import webserver.*;

public class UserListProcessor implements RequestProcessor{

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        if (!request.isMethod(HttpMethod.GET)) {
            throw new MethodNotAllowedException("Only GET method is allowed.");
        }
        HttpHeaders headers = new HttpHeaders();
        String cookie = request.getCookie();
        if (cookie.equals("logined=true")) {
            return new HttpResponse(HttpStatus.OK, headers, Resource.of("/list.html"));
        }
        return new HttpResponse(HttpStatus.OK, headers, Resource.of("/login.html"));
    }
}
