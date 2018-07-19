package webserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.RequestMapping;
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

			log.debug("requestUrl : {} ", httpRequest.getUrl());
			Object controller = ControllerFactory.getController(httpRequest.getUrl());

			if (controller == null) {
				log.debug("controller null");
				httpResponse.forward(getDefaultPath(httpRequest.getUrl()));
				return;
			}
			log.debug("controller exist");

			String path = controller.getClass().getAnnotation(RequestMapping.class).value();
			Method[] methods = controller.getClass().getMethods();

			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					if (httpRequest.urlCorrect(path + method.getAnnotation(RequestMapping.class).value())) {
							method.invoke(controller, httpRequest, httpResponse);
					}
					return;
				}
			}
		} catch (Exception e) {
			log.error("controller exist & annotation : "+e.getMessage());
		}
	}
	
	public String getDefaultPath(String path) {
		if (path.equals("/")) {
			return "/index.html";
		}
		return path;
	}

}
