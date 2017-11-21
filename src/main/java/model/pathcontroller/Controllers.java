package model.pathcontroller;

import java.util.Map;

public class Controllers {
	public static PathController find(String url) {
		Map<String, PathController> pathControllers = PathControllerFactory.createpathControllers();
		PathController controller = pathControllers.get(url);
		if (controller == null) {
			return pathControllers.get("normal");
		}
		return controller;
	}
}
