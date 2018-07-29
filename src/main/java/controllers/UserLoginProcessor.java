package controllers;

import db.DataBase;
import exception.MethodNotAllowedException;
import model.User;
import webserver.*;

import java.util.Map;

public class UserLoginProcessor implements RequestProcessor {

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        if (!request.isMethod(HttpMethod.POST)) {
            throw new MethodNotAllowedException("Only POST method is allowed.");
        }
        Map<String, String> params = request.getParameters();
        User dbUser = DataBase.findUserById(params.get("userId"));
        HttpHeaders headers = new HttpHeaders();
        if (dbUser.isMatch(params.get("password"))) {
            headers.addHeader(HttpHeader.SET_COOKIE, "logined=true; Path=/");
            headers.addHeader(HttpHeader.LOCATION, "/index.html");
            return new HttpResponse(HttpStatus.FOUND, headers, Resource.ofEmpty());
        }
        return new HttpResponse(HttpStatus.FORBIDDEN, headers, Resource.of("/login_failed.html"));
    }
}
