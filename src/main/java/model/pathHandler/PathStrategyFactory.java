package model.pathHandler;

import java.util.HashMap;

public class PathStrategyFactory {
	
	public static HashMap<String, PathStrategy> createpathStrategies(){
		HashMap<String, PathStrategy> pathStrategies = new HashMap<>();
		pathStrategies.put("/user/create", new CreateUserStrategy());
		pathStrategies.put("/user/login", new LoginStrategy());
		pathStrategies.put("/user/list", new UserListStrategy());
		pathStrategies.put("normal", new StaticGetStrategy());
		return pathStrategies;
	}
}
