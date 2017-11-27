package http.api.readbodycontroller;

import java.util.HashMap;
import java.util.Map;

public class ReadBodyControllerFactory {
	public static ReadBodyController find(String path) {
		Map<String, ReadBodyController> readBodyControllers = ReadBodyControllerFactory.createReadBodyControllers();
		ReadBodyController controller = readBodyControllers.get(path);
		if (controller == null) {
			return readBodyControllers.get("normal");
		}
		return controller;
	}

	public static Map<String, ReadBodyController> createReadBodyControllers() {
		Map<String, ReadBodyController> readBodyControllers = new HashMap<>();
		readBodyControllers.put("/user/list.html", new UserListBodyController());
		readBodyControllers.put("normal", new NormalBodyController());
		return readBodyControllers;
	}
}
