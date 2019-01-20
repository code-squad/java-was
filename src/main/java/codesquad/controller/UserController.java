package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.db.DataBase;
import codesquad.model.HttpSession;
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
        DataBase.addUser(user);
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(User user, HttpSession httpSession) {
        log.debug(user.toString());
        httpSession.setAttribute("logined", true);
        return "redirect:/index.html";
    }
}
