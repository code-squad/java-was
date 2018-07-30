package com.larry.web;

import com.larry.db.DataBase;
import com.larry.model.User;
import com.larry.webserver.annotations.Controller;
import com.larry.webserver.annotations.RequestMapping;
import com.larry.webserver.exceptions.ControllerExecuteException;
import com.larry.webserver.exceptions.CustomError;
import com.larry.webserver.exceptions.UnAuthenticationException;
import com.larry.webserver.http.Request;
import com.larry.webserver.http.Response;
import com.larry.webserver.mvc.viewFlow.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.larry.webserver.http.HttpMethod.GET;
import static com.larry.webserver.http.HttpMethod.POST;
import static com.larry.webserver.http.HttpStatus.FOUND;

@RequestMapping(path = "/user")
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private DataBase userRepository = DataBase.getInstance();

    @RequestMapping(method = GET, path = "/list.html")
    public ModelAndView getList(Request request, Response response) {
        if (!request.getCookie()) {
            return ModelAndView.viewOf("index.html");
        }

        List<User> users = userRepository.findAll();
        ModelAndView modelAndView = ModelAndView.viewOf("user/list.html");
        modelAndView.setModel("users", users);

        return modelAndView;
    }

    @RequestMapping(method = GET, path = "/form.html")
    public ModelAndView userCreateForm(Request request, Response response) {
        return ModelAndView.viewOf("user/form.html");
    }

    @RequestMapping(method = GET, path = "/login.html")
    public ModelAndView loginForm(Request request, Response response) {
        return ModelAndView.viewOf("user/login.html");
    }

    @RequestMapping(method = POST, path = "/login")
    public ModelAndView login(Request request, Response response) {
        try {
            Map<String, String> params = request.getParams();
            String userId = params.get("userId");
            String password = params.get("password");
            User user = userRepository.findUserById(userId);
            user.isPassword(password);
            response.loginSuccess();
            return ModelAndView.viewOf("redirect:/index.html");
        } catch (UnAuthenticationException e) {
            CustomError error = new CustomError(e.getMessage(),"/user/login_failed.html");
            throw new ControllerExecuteException(error);
        }
    }

    @RequestMapping(method = POST, path = "/create")
    public ModelAndView userCreate(Request request, Response response) {
        Map<String, String> params = request.getParams();
        User user = new User()
                .setUserId(params.get("userId"))
                .setPassword(params.get("password"))
                .setName(params.get("name"))
                .setEmail(params.get("email"));
        response.setStatue(FOUND);

        log.debug("created user {}", user.toString());
        userRepository.addUser(user);

        return ModelAndView.viewOf("redirect:/index.html");
    }

}
