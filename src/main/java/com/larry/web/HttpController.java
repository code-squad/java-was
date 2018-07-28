package com.larry.web;

import com.larry.db.DataBase;
import com.larry.webserver.annotations.RequestMapping;
import com.larry.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.larry.webserver.annotations.Controller;

import java.util.Map;

@RequestMapping(path = "")
@Controller
public class HttpController {

    private final Logger log = LoggerFactory.getLogger(HttpController.class);

    @RequestMapping(method = "GET", path = "/index.html")
    public String index() {
        return "index.html";
    }

    @RequestMapping(method = "GET", path = "/user/form.html")
    public String userCreateForm() {
        return "user/form.html";
    }

    @RequestMapping(method = "GET", path = "/user/login.html")
    public String loginForm() {
        return "user/login.html";
    }

    @RequestMapping(method = "POST", path = "/user/login")
    public String login(Map<String, String> params) {
        String userId = params.get("userId");
        String password = params.get("password");
        User user = DataBase.getInstance().findUserById(userId);
        user.isPassword(password);
        return "redirect:/index.html";
    }

    @RequestMapping(method = "POST", path = "/user/create")
    public String userCreate(Map<String, String> params) {
        User user = new User()
                .setUserId(params.get("userId"))
                .setPassword(params.get("password"))
                .setName(params.get("name"))
                .setEmail(params.get("email"));

        log.debug("created user {}", user.toString());
        DataBase.getInstance().addUser(user);
        return "redirect:/index.html";
    }

}
