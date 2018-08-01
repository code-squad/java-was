package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.AnnotationHandler;
import model.BeanFactory;
import model.HandlerExecution;
import model.HttpRequest;
import model.HttpResponse;

public class FrontController {

	private static final Logger log = LoggerFactory.getLogger(FrontController.class);

	public static void dispatch(HttpRequest request, HttpResponse response) throws Exception {

		BeanFactory beanFactory = BeanFactory.getInstance();
		
		HandlerExecution controller = beanFactory.getController(request.getUrl());
		if (controller == null) {
			log.debug("controller null");
			response.forward(getDefaultPath(request.getUrl()));
			return;
		}
		log.debug("controller exist");
		AnnotationHandler annotationHandler = AnnotationHandler.of(request,response);
		annotationHandler.controllerHandle(controller);
	}

	public static String getDefaultPath(String path) {
		if (path.equals("/")) {
			return "/index.html";
		}
		return path;
	}
}
