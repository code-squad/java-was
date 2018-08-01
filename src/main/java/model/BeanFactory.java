package model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Autowired;
import annotation.Controller;
import annotation.Repository;
import annotation.RequestMapping;
import annotation.Service;

public class BeanFactory {

	private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
	private Map<String, HandlerExecution> controllers;
	private Map<String, Object> beans;
	private static BeanFactory beanFactory;
	public static Reflections reflections;

	public BeanFactory() {
	}

	public BeanFactory(Map<String, Object> beans) {
		this.beans = beans;
		this.controllers = new HashMap<String, HandlerExecution>();
	}

    public static BeanFactory getInstance() {
        if (beanFactory == null) {
           beanFactory =  BeanFactory.of().init();
           return beanFactory;
        }
        return beanFactory;
    }
	
	public static BeanFactory of() {
		URL packageDirURL = Thread.currentThread().getContextClassLoader().getResource("./");
		reflections = new Reflections(packageDirURL);

		Map<String, Object> beans = new HashMap<String, Object>();
		try {
			addBeans(beans);
		} catch (Exception e) {
			log.debug("addBeans error :"+e.getMessage());
		}
		
		inject(beans);

		return new BeanFactory(beans);
	}

	public BeanFactory init() {
		for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
			addController(clazz);
		}
		return this;
	}

	public static Set<Class<?>> findAnnotatedEntity() {
		Set<Class<?>> annotatedEntities = new HashSet<>();
		annotatedEntities.addAll(reflections.getTypesAnnotatedWith(Repository.class));
		annotatedEntities.addAll(reflections.getTypesAnnotatedWith(Controller.class));
		annotatedEntities.addAll(reflections.getTypesAnnotatedWith(Service.class));
		//언제든 빈을 추가할수있는 확장용이한 구조
		return annotatedEntities;
	}

	public static void addBeans(Map<String, Object> beans) throws Exception {
		for (Class<?> annotatedEntity : findAnnotatedEntity()) {
			beans.put(annotatedEntity.getName(), annotatedEntity.newInstance());
		}
	}

	public void addController(Class<?> clazz) {
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

	public static void inject(Map<String, Object> beans) {
		try {
			Set<String> keys = beans.keySet();
			for (String key : keys) {
				Object bean = beans.get(key);
				dependencyInjection(beans, bean);
			}

		} catch (Exception e) {
			log.debug("inject error");
			e.printStackTrace();
		}
	}

	public static void dependencyInjection(Map<String, Object> beans, Object bean) throws Exception {
		for (Field field : bean.getClass().getDeclaredFields()) {
			field.setAccessible(true); // private access 허용
			if (field.isAnnotationPresent(Autowired.class)) {
				field.set(bean, beans.get(field.getType().getName()));
				log.debug(
						"필드에 주입된 값과 beanFactory에 담긴 값은" + field.get(bean).equals(beans.get(field.getType().getName())));
			}
		}
	}

	public HandlerExecution getController(String requestUrl) {
		Set<String> set = controllers.keySet();
		for (String key : set) {
			if (requestUrl.equals(key)) {
				return controllers.get(key);
			}
		}
		return null;
	}
}