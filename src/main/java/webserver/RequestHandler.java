package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Controller.AbstractController;
import Controller.Controller;
import Controller.CreateUserController;
import Controller.ListUserController;
import Controller.LoginController;

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
			String url = httpRequest.getURI();
			Controller controller = Stream.of(RqstUri.values()).filter(s -> s.getUri().equals(url)).findFirst().orElse(RqstUri.GET_FILE).createController();
			controller.service(httpRequest, httpResponse);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private enum RqstUri {
		USER_CREATE("/user/create", () -> new CreateUserController()), 
		USER_LOGIN("/user/login", () -> new LoginController()), 
		USER_LIST("/user/list", () -> new ListUserController()), GET_FILE("", () -> new AbstractController() {});

		final private String uri;
		final private Supplier<Controller> expression;

		private RqstUri(String uri, Supplier<Controller> expression) {
			this.uri = uri;
			this.expression = expression;
		}

		public Controller createController() {
			return expression.get();
		}

		public String getUri() {
			return uri;
		}
	}
}
