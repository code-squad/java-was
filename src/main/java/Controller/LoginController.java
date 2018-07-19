package Controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import webserver.RequestHandler;

public class LoginController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Override
	void doPost(HttpRequest request, HttpResponse response) throws IOException {

		log.debug("requestUrl : {}", request.getUrl());
		log.debug("userId : {} , password :{}", request.getParameter("userId"), request.getParameter("password"));
		User user = DataBase.findUserById(request.getParameter("userId"));
		if (user == null) {
			log.debug("User Not Found!");
			response.sendRedirect("/user/login_failed.html");
			return;
		}

		if (user.getPassword().equals(request.getParameter("password"))) {
			log.debug("login success!!");
			response.addHeader("Set-Cookie", "logined=true");
			response.sendRedirect();
			return;
		}
		log.debug("password mismatch!!");
		response.sendRedirect("/user/login_failed.html");
	}

	@Override
	void doGet(HttpRequest request, HttpResponse response) throws IOException {

	}

}
