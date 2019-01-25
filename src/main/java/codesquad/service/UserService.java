package codesquad.service;

import codesquad.db.UserRepository;
import codesquad.model.User;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class UserService {
    private static final Logger log = getLogger(UserService.class);

    public static void create(User user) {
        UserRepository.addUser(user);
    }

    public static void login(User user) throws RuntimeException {
        User dbUser = UserRepository.findUserById(user.getUserId());
        if(!dbUser.equals(user)) throw new IllegalStateException();
    }
}
