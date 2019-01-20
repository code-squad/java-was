package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.HttpSession;
import codesquad.model.RequestMethod;
import codesquad.model.User;
import codesquad.service.UserService;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserController {
    private static final Logger log = getLogger(UserController.class);

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(User user) {
        log.debug(user.toString());
        UserService.create(user);
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(User user, HttpSession session) {
        log.debug(user.toString());
        try {
            UserService.login(user);
            session.setAttribute("logined", true);
            return "redirect:/index.html";
        } catch(Exception e) {
            log.error(e.getMessage());
            return "/user/login_failed.html";
        }
    }

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String list(HttpSession session) {
        log.debug(session.toString());
        if(!session.getAttribute("logined").equals("true")) return "/user/login.html";
        return "/user/list.html";
    }
}
