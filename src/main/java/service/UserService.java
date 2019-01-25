package service;

import db.DataBase;
import exception.UnAuthenticationException;
import model.User;

import java.util.ArrayList;
import java.util.List;

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

    public static List<User> findAll() {
        return new ArrayList<>(DataBase.findAll());
    }
}
