package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.RequestMethod;
import codesquad.model.User;
import org.slf4j.Logger;
import codesquad.util.HttpRequestUtils;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserController {
    private static final Logger log = getLogger(UserController.class);

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public User create(String bodyText) {
        Map<String, String> bodyVal = HttpRequestUtils.parseQueryString(bodyText);
        User user = new User(bodyVal.get("userId"),
                bodyVal.get("password"),
                bodyVal.get("name"),
                bodyVal.get("email"));
        log.debug(user.toString());
        return user;
    }
}
