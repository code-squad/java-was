package model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.RequestMapping;

public class ControllerFactory {

	private static final Logger log = LoggerFactory.getLogger(ControllerFactory.class);
	private static final String CONTROLLER_PACKAGE_NAME = "controller";
	private static Map<String, Object> controllers = new HashMap<String, Object>();

	static {

		String packageNameSlashed = "./" + CONTROLLER_PACKAGE_NAME.replace(".", "/");
		URL packageDirURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlashed);
		String directoryString = packageDirURL.getFile();

		File directory = new File(directoryString);
		List<Class<?>> list = new ArrayList<>();
		if (directory.exists()) {
			String[] files = directory.list();
			for (String fileName : files) {
				addClassFileToList(fileName, list);
			}
		}

		for (Class<?> clazz : list) {
			if (clazz.isAnnotationPresent(Controller.class)) {
				addController(clazz);
			}
		}
	}

	public static void addClassFileToList(String fileName, List<Class<?>> list) {
		if (fileName.endsWith(".class")) {
			fileName = fileName.substring(0, fileName.length() - 6); 
		}
		try {
			Class<?> clazz = Class.forName(CONTROLLER_PACKAGE_NAME + "." + fileName); // Dynamic Loading
			list.add(clazz); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void addController(Class<?> clazz) {
		if (clazz.isAnnotationPresent(RequestMapping.class)) {
			log.debug("requestMapping url : {}", clazz.getAnnotation(RequestMapping.class).value());
			try {
				controllers.put(clazz.getAnnotation(RequestMapping.class).value(), clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static Object getController(String requestUrl) {
		Set<String> set = controllers.keySet();
		for (String string : set) {
			if (requestUrl.startsWith(string) && !requestUrl.contains(".")) {
				return controllers.get(string);
			}
		}
		return null;
	}

}
