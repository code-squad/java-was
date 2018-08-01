package util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;


import annotation.RequestMapping;
import annotation.RequestParam;
import model.HttpRequest;
import model.HttpResponse;

public class ReflectionUtils {

	private Parameter[] parameters;
	
	public ReflectionUtils() {
	}

	public ReflectionUtils(Parameter[] parameters) {
		this.parameters = parameters;
	}

	public static String requestPath(Object controller) {
		return controller.getClass().getAnnotation(RequestMapping.class).value();
	}

	public static Method[] getMethods(Object controller) {
		return controller.getClass().getMethods();
	}

	public List<Object> makeParams(List<Object> params, HttpRequest request, HttpResponse response) throws Exception {
		for (Parameter parameter : parameters) {
			if (parameter.isAnnotationPresent(RequestParam.class)) {
				params.add(request.getParameter(parameter.getAnnotation(RequestParam.class).value()));
			}
			if (parameter.getType().equals(HttpResponse.class)) {
				params.add(response);
			}
			if (parameter.getType().equals(HttpRequest.class)) {
				params.add(request);
			}
			checkBeanRule(params, parameter.getType(), request);
		}
		return params;
	}

	public void checkBeanRule(List<Object> params, Class<?> parameterType, HttpRequest request) throws Exception {
		if (checkSetter(parameterType.getMethods()) && checkDefaultConstructer(parameterType)) {
			params.add(createBeanObject(parameterType, request));
		}
	}

	public boolean checkDefaultConstructer(Class<?> parameterType) throws Exception {
		return parameterType.getConstructor() != null;
	}

	public Boolean checkSetter(Method[] methods) {
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
				return true;
			}
		}
		return false;
	}

	public Object createBeanObject(Class<?> parameterType, HttpRequest request) throws Exception {
		Object parmeterObject = parameterType.newInstance();
		for (Method method : parameterType.getMethods()) {
			if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
				method.invoke(parmeterObject, request.getParameter(obtainParameterName(method.getName())));
			}
		}
		return parmeterObject;
	}

	public String obtainParameterName(String propertyName) {
		propertyName = propertyName.substring(3);
		return propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
	}

}