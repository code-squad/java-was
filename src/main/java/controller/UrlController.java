package controller;

import java.util.HashMap;

import request.RequestHeader;
import util.SplitUtils;

public class UrlController {
	private HashMap<String, Controller> urlMap = new HashMap<String, Controller>();
	
	public UrlController() {
		addController("/user/create", new UserCreateController());
		addController("/user/login", new UserLoginController());
		addController("/user/list", new UserListController());
		addController("webFile", new WebFileController());
		addController("/", new HomeController());
	}
	
	public void addController(String url, Controller controller) {
		urlMap.put(url, controller);
	}

	public Controller resolveController(RequestHeader request) {
		String url = request.getPathValue();
		if(isWebFile(url)) {
			return urlMap.get("webFile");
		}
		return urlMap.get(url);
	}
	

	private boolean isWebFile(String url) {
		String extension = SplitUtils.getSplitedExtension(url).toUpperCase();
		return "HTML".equals(extension) || "JS".equals(extension) || "CSS".equals(extension) || "WOFF".equals(extension) || "ICO".equals(extension);
	}
}