package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Controller.Controller;


public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	private Boolean login = false;

	public RequestHandler() {
	}

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();) {
			HttpRequest httpRequest = new HttpRequest(in);
			HttpResponse httpResponse = new HttpResponse(out);
			
			Controller controller = RequestMapping.getController(httpRequest.getURI());
			
			if (controller != null) {
				controller.service(httpRequest, httpResponse);
			} 
			if (controller == null) {
				String uri = httpRequest.getURI();
				httpResponse.forward(defaultPage(uri));
			}
		} catch (IOException e) {
			log.error(e.getMessage());
//			this.run();
		}
	}
	
	private String defaultPage(String uri) {
		if(uri.equals("/")) {
			return "/index.html";
		}
		return uri;
	}
}
