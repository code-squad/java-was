package model.pathhandler;

import java.util.HashMap;

public class PathStrategyFactory {
	
	public static HashMap<String, PathController> createpathControllers(){
		HashMap<String, PathController> pathController = new HashMap<>();
		pathController.put("/user/create", new CreateUserController());
		pathController.put("/user/login", new LoginStrateg());
		pathController.put("/user/list", new UserListController());
		pathController.put("normal", new StaticGetController());
		return pathController;
	}
}
