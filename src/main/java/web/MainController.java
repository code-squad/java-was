package web;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import security.HttpSession;
import service.UserService;
import setting.Controller;
import setting.GetMapping;
import setting.PostMapping;
import webserver.viewresolver.Model;

import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MainController {

    private static final Logger logger = getLogger(MainController.class);

    @GetMapping(value = "/index")
    public String navigate() {
        logger.debug("Call navigate MethodType!");
        return "index.html";
    }

    @GetMapping(value = "/users/form")
    public String joinForm() {
        logger.debug("Call joinForm MethodType!");
        return "/user/form.html";
    }

    @PostMapping(value="/users/create")
    public String join(User user) {
        logger.debug("Call Join MethodType! User : {}", user.toString());
        UserService.saverUser(user);
        return "redirect:/index";
    }

    @GetMapping(value="/users/login")
    public String loginForm() {
        logger.debug("Call loginForm MethodType");
        return "/user/login.html";
    }

    @PostMapping(value="/users/login")
    public String login(HttpSession httpSession, User user) {
        if(!UserService.isJoinUser(user)) {
            logger.debug("로그인 실패! User : {}", user.toString());
            return "/user/login_failed.html";
        }
        logger.debug("로그인 성공! User : {}", user.toString());
        httpSession.addSession(user);
        return "redirect:/index";
    }

    @GetMapping(value="/users/list")
    public String list(HttpSession httpSession, Model model) {
        if(!httpSession.isLoginUser()) {
            logger.debug("로그인하지 않아서 메인화면 이동");
            return "redirect:/users/login";
        }
        model.addAttribute("users", DataBase.findAll().stream().collect(Collectors.toList()));
        return "/user/list.html";
    }

    @GetMapping(value="/css")
    public String css(String filePath) {
        logger.debug("Call css MethodType");
        return filePath;
    }

}
