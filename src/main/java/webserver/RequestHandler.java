package webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
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


			FrontController controller = ControllerFactory.getController(httpRequest.getUrl());

			if (controller == null) {
				log.debug("controller null");
				httpResponse.forward(getDefaultPath(httpRequest.getUrl()));
			} else {
				log.debug("controller exist");
				controller.service(httpRequest, httpResponse);
			}

		} catch (IOException e) {
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
