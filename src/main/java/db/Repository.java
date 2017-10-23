package db;

import model.User;

public interface Repository {

    public User findByUserId(String userId);

    public User findByName(String name);

}
