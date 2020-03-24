package db;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    public static int getSizeOfUsers() {
        return users.size();
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) throws NoSuchElementException{
        return Optional.ofNullable(users.get(userId)).orElseThrow(NoSuchElementException::new);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
