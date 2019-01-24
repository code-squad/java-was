package service;

import db.DataBase;
import exception.UnAuthenticationException;
import model.User;
import util.ObjectMaker;
import webserver.http.request.HttpRequest;

public class UserService {
    public static User addUser(User user) {
        DataBase.addUser(user);
        return user;
    }

    public static User login(User loginUser) throws UnAuthenticationException {
        return DataBase.findUserById(loginUser.getUserId())
                .filter(user -> user.matchPassword(loginUser))
                .orElseThrow(UnAuthenticationException::new);
    }
}
