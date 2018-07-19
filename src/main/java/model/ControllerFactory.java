package model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import annotation.Controller;
import annotation.RequestMapping;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;

public class ControllerFactory {

	private static Map<String, Object> controllers = new HashMap<String, Object>();

	static {

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
			if (class1.isAnnotationPresent(Controller.class)) {
				if (class1.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = (RequestMapping) class1.getAnnotation(RequestMapping.class);
					System.out.println(requestMapping.value());
					try {
						controllers.put(requestMapping.value(),  class1.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// controllers.put(key, value)
//		controllers.put("/user/list", new ListUserController());
	}

	public static Object getController(String requestUrl) {

		Set<String> set = controllers.keySet();
		System.out.println(requestUrl+" requestUrl");
		for (String string : set) {
			if (requestUrl.startsWith(string) && !requestUrl.contains(".")) {
				return controllers.get(string);
			}
		}

		return null;
	}

}
