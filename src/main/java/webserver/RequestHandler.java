package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.Method;
import model.User;
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
			HttpResponse response = null;

			if (Method.Post.equals(request.getHeader("method"))) {
				response = Http302Response.create();
				handlePost(request, response);
			}
			if (Method.Get.equals(request.getHeader("method"))) {
				response = Http200Response.create();
				handleGet(request, response);
			}
			response.writeResponse(dos);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void handleGet(HttpRequest request, HttpResponse response) throws IOException {
		String url = request.getHeader("url");
		if ("/user/list".equals(url)) {
			if (request.isLogined()) {
				response.setUrl("/user/list.html");
				response.putBody(DataBase.addUserList(Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath())));
			} else {
				response.setUrl("/user/login.html");
			}
		} else {
			response.setUrl(url);
			response.setContentType(request.getHeader("Accept"));
		}
	}

	private void handlePost(HttpRequest request, HttpResponse response) {
		String url = request.getUrl();
		if ("/user/login".equals(url)) {
			User user = DataBase.findUserById(request.getParameter("userId"));
			if (user != null && user.matchPassword(request.getParameter("password"))) {
				response.setCookieLogined(true);
				response.setUrl("/index.html");
			} else {
				response.setCookieLogined(false);
				response.setUrl("/user/login_failed.html");
			}
		}
		if ("/user/create".equals(url)) {
			DataBase.addUser(new User(request.getParameter("userId"), request.getParameter("password"),
					request.getParameter("name"), request.getParameter("email")));
			response.setUrl("/index.html");
		}
	}
}
