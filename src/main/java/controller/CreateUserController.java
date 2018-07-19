package controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

@Controller
@RequestMapping("/create")
public class CreateUserController {
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);


	@RequestMapping("")
	public void create(HttpRequest request, HttpResponse response) {
		log.debug("requestUrl : {}",request.getUrl());
		
			User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"),
					request.getParameter("email"));
			log.debug("User : {}", user.toString());

			DataBase.addUser(user);
			response.sendRedirect();
	}



}
