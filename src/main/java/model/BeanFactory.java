package model;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Autowired;
import annotation.Controller;
import annotation.Repository;
import annotation.RequestMapping;

public class BeanFactory {

	private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
	private static Map<String, HandlerExecution> controllers = new HashMap<String, HandlerExecution>();
	private static Map<String, Object> beans = new HashMap<String, Object>();
	private static String ROOT;

	static {

		URL packageDirURL = Thread.currentThread().getContextClassLoader().getResource("./");
		ROOT = packageDirURL.getFile();
		log.debug("ROOT : " + ROOT);

		File directory = new File(ROOT);
		List<Class<?>> classes = new ArrayList<>();

		if (directory.exists()) {
			scan(directory.listFiles(), classes, ROOT);
		}
		addBeans(classes);
		inject();

		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(Controller.class)) {
				addController(clazz);
			}
		}
	}

	public static void addBeans(List<Class<?>> classes) {
		for (Class<?> clazz : classes) {
			addControllerBean(clazz);
			addRepository(clazz);
		}
	}

	public static void scan(File[] files, List<Class<?>> classes, String presentPath) {
		for (File file : files) {
			isFile(file, classes, presentPath);
		}
	}

	public static void isFile(File file, List<Class<?>> classes, String presentPath) {
		if (file.isDirectory()) {
			scan(file.listFiles(), classes, presentPath + file.getName());
			return;
		}
		if (file.getName().endsWith(".class")) {
			addClassFileToList(file.getName(), presentPath, classes);
		}
	}

	public static void addClassFileToList(String fileName, String directoryName, List<Class<?>> classes) {
		try {
			Class<?> clazz = Class.forName(
					directoryName.substring(ROOT.length()) + "." + fileName.substring(0, fileName.length() - 6)); // Dynamic Loading
			classes.add(clazz);
			log.debug("Path ! : {}", directoryName);
			log.debug("File Name! : {}", fileName);
		} catch (ClassNotFoundException e) {
			log.debug("addClassFileToList error");
			e.printStackTrace();
		}
	}

	public static void addController(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(RequestMapping.class)) {
				log.debug("requestMapping url : {}", clazz.getAnnotation(RequestMapping.class).value());
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					HandlerExecution handleExecution = new HandlerExecution(method, beans.get(clazz.getName()));
					if (handleExecution.hasRequestAnnotation()) {
						controllers.put(handleExecution.pullMethodRequest(), handleExecution);
					}
				}
			}
		} catch (Exception e) {
			log.debug("addController error");
			e.printStackTrace();
		}
	}

	public static void addControllerBean(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(Controller.class)) {
				log.debug("clazz name" + clazz.getName());
				beans.put(clazz.getName(), clazz.newInstance());
			}
		} catch (Exception e) {
			log.debug("addControllerBean error");
			e.printStackTrace();
		}
	}

	public static void addRepository(Class<?> clazz) {
		try {
			if (clazz.isAnnotationPresent(Repository.class)) {
				log.debug("clazz name" + clazz.getName());

				beans.put(clazz.getName(), clazz.newInstance());
			}
		} catch (Exception e) {
			log.debug("addRepository error");
			e.printStackTrace();
		}
	}

	public static void inject() {
		try {
			Set<String> keys = beans.keySet();
			for (String key : keys) {
				Object bean = beans.get(key);
				injectData(bean.getClass().getDeclaredFields(), bean);
			}

		} catch (Exception e) {
			log.debug("addRepository error");
			e.printStackTrace();
		}
	}

	public static void injectData(Field[] fields, Object bean) throws Exception {
		for (Field field : fields) {
			field.setAccessible(true); //private access 허용
			if (field.isAnnotationPresent(Autowired.class)) {
				field.set(bean, beans.get(field.getType().getName()));
				log.debug("필드에 주입된 값과 beanFactory에 담긴 값은" + field.get(bean).equals(beans.get(field.getType().getName())));
			}
		}
	}

	public static HandlerExecution getController(String requestUrl) {
		Set<String> set = controllers.keySet();
		for (String key : set) {
			if (requestUrl.equals(key)) {
				return controllers.get(key);
			}
		}
		return null;
	}
}