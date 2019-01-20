package codesquad.db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import codesquad.model.User;

public class UserRepository {

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    static {
        // test data
        users.put("brad903", new User("brad903", "1234", "brad", "brad903@naver.com"));
        users.put("alex407", new User("alex407", "1234", "alex", "alex@naver.com"));
        users.put("leejh903", new User("leejh903", "1234", "leejunghyun", "junghyun@naver.com"));
    }
}
