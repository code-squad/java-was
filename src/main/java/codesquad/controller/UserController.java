package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.Cookie;
import codesquad.model.RequestMethod;
import codesquad.model.User;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserController {
    private static final Logger log = getLogger(UserController.class);

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(User user) {
        log.debug(user.toString());
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(User user, Cookie cookie) {
        log.debug(user.toString());
        cookie.setAttribute("logined", true);
        return "redirect:/index.html";
    }
}
