package webserver;

import model.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    User user1, user2;
    List<User> users;

    @Before
    public void setUp() {
        user1 = new User("id1", "password1", "name1", "jwb8705@gmail.com");
        user2 = new User("id2", "password2", "name2", "jwb8705@naver.com");
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
    }

    @Test
    public void createDynamicHTML() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("HttpResponse.txt"));
        byte[] body = response.createDynamicHTML("./webapp/user/list_static.html", users);
        response.responseBody(body);
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
