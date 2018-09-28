package util;

import model.User;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelUtilsTest {

    @Ignore
    @Test
    public void createUser_GET() throws Exception {
        String line = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        if (!HttpRequestUtils.parsePath(line).equals("/user/create")) {
            throw new Exception("not user create");
        }

        assertThat(ModelUtils.createUser(line).getUserId(), is("javajigi"));
    }

    @Test
    public void createUser_POST() {
        String line = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        Map<String, String> userData = HttpRequestUtils.parseQueryString(line);

        User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));

        assertThat(user.getUserId(), is("javajigi"));
        assertThat(user.getPassword(), is("password"));
    }
}
