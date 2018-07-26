package webserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.FrontController;
import model.HttpRequest;
import model.HttpResponse;

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
			FrontController.dispatch(httpRequest, httpResponse);
			
		} catch (Exception e) {
			log.error("controller exist & annotation : "+e.getMessage());
		}
	}
	


}
