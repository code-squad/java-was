package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.HttpRequest;
import model.Method;
import model.pathhandler.PathController;
import model.pathhandler.PathStrategyFactory;
import model.response.Http200Response;
import model.response.Http302Response;
import model.response.HttpResponse;

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
			DataOutputStream dos = new DataOutputStream(out);
			HttpRequest request = new HttpRequest(in);
			HttpResponse response = Http200Response.create();
			Map<String, PathController> pathControllers = PathStrategyFactory.createpathControllers();

			if (Method.Post.equals(request.getHeader("method"))) {
				PathController Controller = pathControllers.get(request.getHeader("url"));
				response = Http302Response.create();
				Controller.handling(request, response);
			}
			if (Method.Get.equals(request.getHeader("method"))) {
				PathController handler = pathControllers.get(request.getHeader("url"));
				if (handler == null) {
					handler = pathControllers.get("normal");
				}
				handler.handling(request, response);
			}
			response.writeResponse(dos);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
