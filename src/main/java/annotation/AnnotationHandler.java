package annotation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.HandlerExecution;
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

	public void controllerHandle(HandlerExecution controller) throws Exception {
		processResponse(parameterHandle(controller));
	}

	public void processResponse(String location) {
		log.debug("location : {}", location);
		if (location != null) {
			if (location.startsWith("redirect:")) {
				response.sendRedirect(location.substring(location.indexOf(":") + 1));
			}
		}
	}

	public String parameterHandle(HandlerExecution controller) throws Exception {
		List<Object> params = new ArrayList<>();
		try {
			ReflectionUtils reflectionUtils = new ReflectionUtils(controller.pullParameters());
			params = reflectionUtils.makeParams(params, request, response);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return controller.execute(params);
	}

}
