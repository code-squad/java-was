package com.larry.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import com.larry.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBase {

    private final Logger log = LoggerFactory.getLogger(DataBase.class);

    private static DataBase dataBase = new DataBase();

    public static DataBase getInstance() {
        return dataBase;
    }

    private Map<String, User> users = Maps.newHashMap();

    public User addUser(User user) {
        log.debug("add user : {}", user);
        return users.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        User findUser = users.get(userId);
        log.debug("find by user id : {}", findUser);
        return findUser;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
