package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Controller.Controller;
import Controller.CreateUserController;
import Controller.ListUserController;
import Controller.LoginController;
import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.RequestMapping;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

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

			Controller controller = RequestMapping.getController(httpRequest.getUrl());

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
