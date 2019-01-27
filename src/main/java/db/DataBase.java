package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    static {
        /* 로그인 반복적으로 수행하는 것이 불편해서 테스트 아이디 생성 */
        users.put("javajigi", new User("javajigi", "password", "Pobi", "slipp@naver.com"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
