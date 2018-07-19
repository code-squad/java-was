package model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotation.Controller;
import annotation.RequestMapping;
import controller.FrontController;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;

public class ControllerFactory {

	private static Map<String, FrontController> controllers = new HashMap<String, FrontController>();
	
	static {
		controllers.put("/create", new  CreateUserController());
//		controllers.put("/user/login", new  LoginController());
		
		String packageName = "controller";
		String packageNameSlashed = "./" + packageName.replace(".", "/");
		URL packageDirURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlashed);

		String directoryString = packageDirURL.getFile();

		File directory = new File(directoryString);
		List<Class> list = new ArrayList<>();
		if (directory.exists()) {
			String[] files = directory.list();
			for (String fileName : files) {
				if (fileName.endsWith(".class")) {
					fileName = fileName.substring(0, fileName.length() - 6); // 확장자 삭제
				}
				try {
					Class c = Class.forName(packageName + "." + fileName); // Dynamic Loading
					list.add(c); // List<Class> list 에 넣는다
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		for (Class class1 : list) {
			if(class1.isAnnotationPresent(Controller.class)) {
				if(class1.isAnnotationPresent(RequestMapping.class)){
					RequestMapping requestMapping = (RequestMapping) class1.getAnnotation(RequestMapping.class);
					System.out.println(requestMapping.value());
					try {
						controllers.put(requestMapping.value(), (FrontController)class1.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
//		controllers.put(key, value)
		controllers.put("/user/list", new  ListUserController());
	}
	
	public static FrontController getController(String requestUrl) {
		if(requestUrl.startsWith("/user/list")) {
			requestUrl = "/user/list";
		}
		return controllers.get(requestUrl);
	}
	
	
}
