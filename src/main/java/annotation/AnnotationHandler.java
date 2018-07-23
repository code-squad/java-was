package annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.HttpRequest;
import model.HttpResponse;
import util.ReflectionUtils;

public class AnnotationHandler {

	private static final Logger log = LoggerFactory.getLogger(AnnotationHandler.class);
	private HttpRequest request;
	private HttpResponse response;

	public AnnotationHandler() {
	}

	public AnnotationHandler(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
	}

	public static AnnotationHandler of(HttpRequest request, HttpResponse response) {
		return new AnnotationHandler(request, response);
	}

	public void controllerHandle(Object controller) throws Exception {
		Method[] methods = ReflectionUtils.getMethods(controller);

		for (Method method : methods) {
			if (method.isAnnotationPresent(RequestMapping.class)) {
				isMatchUrl(controller, method);
				return;
			}
		}
	}

	public void isMatchUrl(Object controller, Method method) throws Exception {
		if (request.urlCorrect(ReflectionUtils.requestPath(controller) + method.getAnnotation(RequestMapping.class).value())) {
			processResponse(parameterHandle(method, controller));
		}
	}
	
	public void processResponse(String location) {
		log.debug("location : {}", location);
		if (location != null) {
			if (location.startsWith("redirect:")) {
				response.sendRedirect(location.substring(location.indexOf(":") + 1));
			}
		}
	}

	public String parameterHandle(Method method, Object controller) throws Exception {
		List<Object> params = new ArrayList<>();
		try {
			Parameter[] parameters = method.getParameters();
			ReflectionUtils reflectionUtils = new ReflectionUtils(parameters);
			params = reflectionUtils.makeParams(params, request, response);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return (String) method.invoke(controller, params.toArray());
	}

}
