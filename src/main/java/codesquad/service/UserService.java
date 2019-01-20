package codesquad.service;

import codesquad.db.UserRepository;
import codesquad.model.User;

public class UserService {

    public static void create(User user) {
        UserRepository.addUser(user);
    }

    public static void login(User user) throws IllegalStateException {
        User dbUser = UserRepository.findUserById(user.getUserId());
        if(!dbUser.equals(user)) throw new IllegalStateException();
    }
}
