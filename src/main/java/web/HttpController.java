package web;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestMapping;

import java.util.Map;

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

    @RequestMapping(method = "GET", path = "/user/create")
    public String userCreate(Map<String, String> params) {
        User user = new User()
                .setUserId(params.get("userId"))
                .setPassword(params.get("password"))
                .setName(params.get("name"))
                .setEmail(params.get("email"));

        log.debug("created user {}", user.toString());

        DataBase.getInstance().addUser(user);
        // TODO create user and return login success view
        return "index.html";
    }

}
