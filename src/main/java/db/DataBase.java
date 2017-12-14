package db;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    
	private static final Logger log = LoggerFactory.getLogger(DataBase.class);


    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        log.debug(user.getUserId() + " 유저 회원가입 완료!");
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
