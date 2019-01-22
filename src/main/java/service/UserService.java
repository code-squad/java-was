package service;

import db.DataBase;
import model.User;

public class UserService {

    public static void saverUser(User user) {
        DataBase.addUser(user);
    }
}
