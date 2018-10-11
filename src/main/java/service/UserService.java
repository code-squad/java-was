package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {
    private static final Logger log =  LoggerFactory.getLogger(UserService.class);

    public User create(String body) {
        log.debug("create body : {}", body);
        Map<String, String> userData = HttpRequestUtils.parseQueryString(body);
        return new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
    }

    public void save(User user) {
        log.debug("save user : {}", user.toString());
        DataBase.addUser(user);
    }


    public boolean login(String loginBody) {
        Map<String, String> loginData = HttpRequestUtils.parseQueryString(loginBody);
        log.debug("login body : {}", loginBody);
        User loginUser = DataBase.findUserById(loginData.get("userId"));

        if (loginUser == null) {
            return false;
        }

        return loginUser.matchPassword(loginData.get("password"));
    }

    public String list() {
        StringBuilder sb = new StringBuilder();
        List<User> userData = new ArrayList<>(DataBase.findAll());

        sb.append("<html>").append("<head>").append("<body>");
        for (User user : userData) {
            sb.append("user name : ").append(user.getUserId()).append("\r\n");
        }
        sb.append("</body>").append("</head>").append("</html>");

        return sb.toString();
    }
}
