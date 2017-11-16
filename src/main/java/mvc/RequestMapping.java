package mvc;

import java.util.Map;

import com.google.common.collect.Maps;

public class RequestMapping {
	Map<String, Controller> controllers = Maps.newHashMap();

	public 	RequestMapping(){
		init();
	}
	public void init() {
		controllers.put("/user/create", new UserCreateController());
		controllers.put("/user/login", new LoginController());
		controllers.put("/user/list", new UserListController());
	}
	
	public Controller getController(String path) {
		
		if(controllers.get(path)!=null) {
			return controllers.get(path);
		}
		return new BaseController();
	}
	

}
