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
		return 	controller.getClass().getAnnotation(RequestMapping.class).value();
	}
	public static Method[] getMethods(Object controller) {
		return 	controller.getClass().getMethods();
	}
	
	public List<Object> makeParams(List<Object> params, HttpRequest request, HttpResponse response) {
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
		}
		return params;
	}

}