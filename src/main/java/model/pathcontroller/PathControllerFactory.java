package model.pathcontroller;

import java.util.HashMap;

public class PathControllerFactory {
	
	public static HashMap<String, PathController> createpathControllers(){
		HashMap<String, PathController> pathController = new HashMap<>();
		pathController.put("/user/create", new CreateUserController());
		pathController.put("/user/login", new LoginController());
		pathController.put("/user/list", new UserListController());
		pathController.put("normal", new StaticGetController());
		return pathController;
	}
}
