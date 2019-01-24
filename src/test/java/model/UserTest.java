package model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void matchPassword() {
        User user = new User("test", "1234", null, null);
        User loginUser = new User("test", "1234", null, null);

        assertTrue(user.matchPassword(loginUser));
    }
}