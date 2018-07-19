package webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.RequestMapping;
import controller.FrontController;
import model.HttpRequest;
import model.HttpResponse;
import model.ControllerFactory;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			HttpRequest httpRequest = HttpRequest.of(in);
			HttpResponse httpResponse = new HttpResponse(out);

			log.debug("requestUrl : {} " , httpRequest.getUrl());
			Object controller = ControllerFactory.getController(httpRequest.getUrl());

			if (controller == null) {
				log.debug("controller null");
				httpResponse.forward(getDefaultPath(httpRequest.getUrl()));
			} else {
				log.debug("controller exist");
				
				
				String path = controller.getClass().getAnnotation(RequestMapping.class).value();
				Method[] method = controller.getClass().getMethods();
				
				for (Method method2 : method) {
					if(method2.isAnnotationPresent(RequestMapping.class)) {
						if(httpRequest.getUrl().equals(path+method2.getAnnotation(RequestMapping.class).value())) {
							try {
								method2.invoke(controller, httpRequest, httpResponse);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								System.out.println("앗");
								e.printStackTrace();
							}
						}
					}
				}
				
				
			}

		} catch (IOException e) {
			System.out.println("D");
			log.error(e.getMessage());
		}
	}

	public String getDefaultPath(String path) {
		if (path.equals("/")) {
			return "/index.html";
		}
		return path;
	}

}
