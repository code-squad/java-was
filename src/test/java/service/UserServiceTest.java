package service;

import model.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserServiceTest {
    @Test
    public void createUser() {
        UserService userService = new UserService();
        String body = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        User user = userService.createUser(body);
        assertThat(user.getUserId(), is("javajigi"));
        assertThat(user.getPassword(), is("password"));
        assertThat(user.getName(), is("%EB%B0%95%EC%9E%AC%EC%84%B1"));
        assertThat(user.getEmail(), is("javajigi%40slipp.net"));
    }
}
