package util;

import db.DataBase;
import model.User;
import org.junit.Test;
import web.HttpController;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpControllerTest {

    @Test
    public void createUser() {


        Map<String, String> params = new HashMap<>();
        params.put("userId", "javajigi");
        params.put("password", "test");
        params.put("name", "hello");
        params.put("email", "hello@gmail.com");

        User user = new User()
                .setUserId("javajigi")
                .setPassword("test")
                .setName("hello")
                .setEmail("hello@gmail.com");

        HttpController httpController = new HttpController();
        String viewName = httpController.userCreate(params);

        DataBase dataBase = DataBase.getInstance();
        assertThat(viewName, is("redirect:/index.html"));
        assertThat(dataBase.findUserById("javajigi"), is(user));
    }
}
