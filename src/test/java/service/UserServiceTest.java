package service;

import db.DataBase;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.DatabaseMetaData;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class UserServiceTest {
    private UserService userService = new UserService();
    private User defaultUser = new User("learner", "password", "taewon", "tw@gmail.com");

    @Test
    public void create() {
        String body = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        User user = userService.create(body);
        assertThat(user.getUserId(), is("javajigi"));
        assertThat(user.getPassword(), is("password"));
        assertThat(user.getName(), is("%EB%B0%95%EC%9E%AC%EC%84%B1"));
        assertThat(user.getEmail(), is("javajigi%40slipp.net"));
    }

    @Test
    public void save() {
        assertNull(DataBase.findUserById("learner"));
        userService.save(defaultUser);
        assertNotNull(DataBase.findUserById("learner"));
    }

    @Test
    public void login() {
        userService.save(defaultUser);

        String loginUserBody = "userId=learner&password=password";
        String notUserBody = "userId=hacker&password=getroot";

        assertThat(userService.login(loginUserBody), is(true));
        assertThat(userService.login(notUserBody), is(false));

    }
}
