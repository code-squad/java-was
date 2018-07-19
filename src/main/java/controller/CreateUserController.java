package controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class CreateUserController extends AbstractController  {
	private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

	
	@Override
	void doPost(HttpRequest request, HttpResponse response) {
		log.debug("requestUrl : {}",request.getUrl());
		
			User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"),
					request.getParameter("email"));
			log.debug("User : {}", user.toString());

			DataBase.addUser(user);
			response.sendRedirect();
	}


	@Override
	void doGet(HttpRequest request, HttpResponse response) throws IOException {
	}


}
