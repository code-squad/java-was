package util;

import model.User;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelUtilsTest {
    @Test
    public void createUser() throws Exception {
        String line = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        if (!HttpRequestUtils.parsePathFromUrl(line).equals("/user/create")) {
            throw new Exception("not user create");
        }

        Map<String, String> userData = HttpRequestUtils.parseQueryString(line.split(" ")[1].split("\\?")[1]);
        User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
        assertThat(user.getUserId(), is("javajigi"));

        assertThat(ModelUtils.createUser(line).getUserId(), is("javajigi"));
    }
}
