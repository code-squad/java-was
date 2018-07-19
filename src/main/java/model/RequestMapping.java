package model;

import java.util.HashMap;
import java.util.Map;

import Controller.Controller;
import Controller.CreateUserController;
import Controller.ListUserController;
import Controller.LoginController;

public class RequestMapping {

	private static Map<String, Controller> controllers = new HashMap<String, Controller>();
	
	static {
		controllers.put("/create", new  CreateUserController());
		controllers.put("/user/login", new  LoginController());
		controllers.put("/user/list", new  ListUserController());
	}
	
	public static Controller getController(String requestUrl) {
		if(requestUrl.startsWith("/user/list")) {
			requestUrl = "/user/list";
		}
		return controllers.get(requestUrl);
	}
	
	
}
