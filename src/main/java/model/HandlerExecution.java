package model;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import annotation.RequestMapping;

public class HandlerExecution {

	private Method method;
	private Object controller;

	public HandlerExecution() {
	}

	public HandlerExecution(Method method, Object controller) {
		this.method = method;
		this.controller = controller;
	}

	public String pullClassRequest() throws Exception {
		if (!controller.getClass().isAnnotationPresent(RequestMapping.class)) {
			throw new Exception("no mapping class error");
		}
		return controller.getClass().getAnnotation(RequestMapping.class).value();
	}

	public String pullMethodRequest() throws Exception {
		return pullClassRequest() + method.getAnnotation(RequestMapping.class).value();
	}
	
	public Boolean hasRequestAnnotation() {
		return method.isAnnotationPresent(RequestMapping.class);
	}

	public Parameter[] pullParameters() {
		return method.getParameters();
	}

	public String execute(List<Object> params) throws Exception {
		return (String) method.invoke(controller, params.toArray());
	}

}
