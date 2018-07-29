package com.larry.web;

import com.larry.db.DataBase;
import com.larry.webserver.ModelAndView;
import com.larry.webserver.Request;
import com.larry.webserver.Response;
import com.larry.webserver.annotations.RequestMapping;
import com.larry.model.User;
import com.larry.webserver.exceptions.ControllerExecuteException;
import com.larry.webserver.exceptions.CustomError;
import com.larry.webserver.exceptions.UnAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.larry.webserver.annotations.Controller;

import java.util.Map;

import static com.larry.webserver.HttpStatus.FOUND;

@RequestMapping(path = "")
@Controller
public class HttpController {

    private final Logger log = LoggerFactory.getLogger(HttpController.class);

    @RequestMapping(method = "GET", path = "/index.html")
    public ModelAndView index(Request request, Response response) {
        return ModelAndView.viewOf("index.html");
    }

    @RequestMapping(method = "GET", path = "/user/form.html")
    public ModelAndView userCreateForm(Request request, Response response) {
        return ModelAndView.viewOf("user/form.html");
    }

    @RequestMapping(method = "GET", path = "/user/login.html")
    public ModelAndView loginForm(Request request, Response response) {
        return ModelAndView.viewOf("user/login.html");
    }

    @RequestMapping(method = "POST", path = "/user/login")
    public ModelAndView login(Request request, Response response) { // 만약 이런 애들이 있으면 어떡할건데?
        try {
            Map<String, String> params = request.getParams();
            String userId = params.get("userId");
            String password = params.get("password");
            User user = DataBase.getInstance().findUserById(userId);
            user.isPassword(password);

            response.setStatue(FOUND); // default는 OK로 하자
            response.loginSuccess();

            return ModelAndView.viewOf("index.html");
        } catch (UnAuthenticationException e) {
            CustomError error = new CustomError(e.getMessage(),"/user/login_failed.html");
            throw new ControllerExecuteException(error);
        }
    }

    @RequestMapping(method = "POST", path = "/user/create")
    public ModelAndView userCreate(Request request, Response response) {
        Map<String, String> params = request.getParams();
        User user = new User()
                .setUserId(params.get("userId"))
                .setPassword(params.get("password"))
                .setName(params.get("name"))
                .setEmail(params.get("email"));
        response.setStatue(FOUND); // default는 OK로 하자
        log.debug("created user {}", user.toString());
        DataBase.getInstance().addUser(user);
        return ModelAndView.viewOf("index.html");
    }

}
