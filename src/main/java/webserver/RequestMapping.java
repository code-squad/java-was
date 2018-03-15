package webserver;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static Map<String, Controller> controllers = new HashMap<>();

    public static void createControllers(){
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new UserListController());
    }

    public static Controller getController(String requestPath){
        return controllers.get(requestPath);
    }

}
