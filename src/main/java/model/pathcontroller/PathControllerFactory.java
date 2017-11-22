package model.pathcontroller;

import java.util.HashMap;
import java.util.Map;

public class PathControllerFactory {
	
	public static Map<String, PathController> createpathControllers(){
		Map<String, PathController> pathController = new HashMap<>();
		pathController.put("/user/create", new CreateUserController());
		pathController.put("/user/login", new LoginController());
		pathController.put("/user/list", new UserListController());
		pathController.put("normal", new StaticGetController());
		return pathController;
	}
}
