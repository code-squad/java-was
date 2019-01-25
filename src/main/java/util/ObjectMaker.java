package util;

import webserver.http.request.RequestBody;
import model.User;

public class ObjectMaker {

    public static User makeNewUser(RequestBody requestBody) {
        if (requestBody.isEmpty())
            return null;
        return new User(
                requestBody.getBodyValue("userId"),
                requestBody.getBodyValue("password"),
                requestBody.getBodyValue("name"),
                requestBody.getBodyValue("email")
        );
    }

}
