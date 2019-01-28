package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class UserService {

    private static final Logger logger = getLogger(UserService.class);

    public static void saverUser(User user) {
        DataBase.addUser(user);
    }

    public static boolean isJoinUser(User user) {
        User savedUser = DataBase.findUserById(user.getUserId());

        if(user.isJoinUser(savedUser)) {
            return true;
        }

        return false;
    }
}
