package controller;

import java.util.HashMap;

public class UrlController {
	private HashMap<String, Controller> urlMap = new HashMap<String, Controller>();
	
	public UrlController() {
		addController("/user/create", new UserCreateController());
		addController("/user/login", new UserLoginController());
	}
	
	public void addController(String url, Controller controller) {
		urlMap.put(url, controller);
	}

	public Controller resolveController(String url) {
		return urlMap.get(url);
	}
}
