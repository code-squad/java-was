package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.UrlController;
import request.GeneralHeaderValue;
import request.RequestHeader;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	private RequestHeaderHandler requestHeaderHandler;
	private PathFileReader pathFileReader;
	private UrlController urlController;

	public RequestHandler(Socket connectionSocket, UrlController urlController) {
		this.connection = connectionSocket;
		pathFileReader = new PathFileReader("./webapp");
		this.urlController = urlController;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			DataOutputStream dos = new DataOutputStream(out);
			requestHeaderHandler = new RequestHeaderHandler(in);

			RequestHeader request = requestHeaderHandler.getRequestHeader();
			
			Controller resolveController = urlController.resolveController(request);
			log.debug("ResolveController: "+ resolveController.getClass().getName());
			String responseUrl = resolveController.run(request);
			GeneralHeaderValue responseHeaderValue = resolveController.getResponseHeaderValue();

			ResponseHeaderHandler responseHeaderHandler = new ResponseHeaderHandler(responseUrl, pathFileReader,
					responseHeaderValue);
			responseHeaderHandler.response(dos);

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
