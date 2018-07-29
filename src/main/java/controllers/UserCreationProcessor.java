package controllers;

import db.DataBase;
import exception.MethodNotAllowedException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.*;

import java.util.Map;

public class UserCreationProcessor implements RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(UserCreationProcessor.class);

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        if (!request.isMethod(HttpMethod.POST)) {
            throw new MethodNotAllowedException("Only POST method is allowed");
        }
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> userParams = request.getParameters();
        User user = new User(userParams.get("userId"), userParams.get("password"), userParams.get("name"), userParams.get("email"));
        DataBase.addUser(user);
        logger.debug("Created User: {}", user);

        headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8");
        headers.addHeader(HttpHeader.LOCATION, "/index.html");
        return new HttpResponse(HttpStatus.FOUND, headers , Resource.ofEmpty());
    }
}
