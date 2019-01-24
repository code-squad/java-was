package db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    static{
        DataBase.addUser(new User("n", "n", "엔삼", "n@n.com"));
        DataBase.addUser(new User("n1", "n1", "엔삼1", "n1@n.com"));
        DataBase.addUser(new User("n2", "n2", "엔삼2", "n2@n.com"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
