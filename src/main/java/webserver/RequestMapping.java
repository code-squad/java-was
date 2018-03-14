package webserver;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    public static Map<String, Controller> createController(){
        Map<String, Controller> controllers = new HashMap<>();
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new UserListController());
        return controllers;
    }
}
