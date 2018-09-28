package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ModelUtils {
    private static final Logger log =  LoggerFactory.getLogger(ModelUtils.class);

    public static User createUser(String line) {
        Map<String, String> userData = HttpRequestUtils.parseQueryString(line);
        return new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
    }
}
