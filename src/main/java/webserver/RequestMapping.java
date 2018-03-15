package webserver;

import java.util.HashMap;
import java.util.Map;

import Controller.Controller;
import Controller.CreateUserController;
import Controller.ListUserController;
import Controller.LoginController;

public class RequestMapping {
	public static Map<String, Controller> controllers = new HashMap<>();

	static {
		controllers.put("/user/create", new CreateUserController());
		controllers.put("/user/login", new LoginController());
		controllers.put("/user/list", new ListUserController());
	}
	
	public static Controller getController(String uri) {
		return controllers.get(uri);
	}
}
