package http.api.pathcontroller;

import java.util.HashMap;
import java.util.Map;

public class PathControllerFactory {
	public static PathController find(String url) {
		Map<String, PathController> pathControllers = PathControllerFactory.createpathControllers();
		PathController controller = pathControllers.get(url);
		if (controller == null) {
			return pathControllers.get("normal");
		}
		return controller;
	}
	
	public static Map<String, PathController> createpathControllers(){
		Map<String, PathController> pathController = new HashMap<>();
		pathController.put("/user/create", new CreateUserController());
		pathController.put("/user/login", new LoginController());
		pathController.put("/user/list", new UserListController());
		pathController.put("normal", new StaticGetController());
		return pathController;
	}
}
