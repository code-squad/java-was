package webserver;

import controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static final String URL_CREATE = "/user/create";
    private static final String URL_LOGIN = "/user/login";
    private static final String URL_USER_LIST = "/user/list";
    private Map<String, AbstractController> controllerMapping = new HashMap<>();

    public RequestMapping() {
        controllerMapping.put(URL_CREATE, new CreateUserController());
        controllerMapping.put(URL_LOGIN, new LoginController());
        controllerMapping.put(URL_USER_LIST, new ListUserController());
    }

    public AbstractController getController(String path) {
        AbstractController controller = controllerMapping.get(path);
        if (controller == null) {
            controller = new MainController();
        }
        return controller;
    }
}
