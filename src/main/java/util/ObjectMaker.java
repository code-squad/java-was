package util;

import webserver.http.request.HttpRequest;
import model.User;

public class ObjectMaker {

    public static User makeNewUser(HttpRequest request) {
        return new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
    }

}
