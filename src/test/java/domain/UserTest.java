package domain;

import model.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {
    @Test
    public void canCreate() {
        User user = new User(null, null, null, null);
    }

    @Test
    public void matchPassword() {
        User user = new User("learner", "password", "taewon", "tw@gamil.com");
        assertThat(user.matchPassword("password"), is(true));
        assertThat(user.matchPassword("password2"), is(false));
    }
}
