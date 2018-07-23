package controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import db.DataBase;
import model.User;

@Controller
@RequestMapping("/create")
public class CreateUserController{
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);


	@RequestMapping("")
	public String create(@RequestParam("userId") String userId, @RequestParam("password") String password,
			@RequestParam("name") String name, @RequestParam("email") String email) {
		
			User user = new User(userId, password, name,
					email);
			log.debug("User : {}", user.toString());

			DataBase.addUser(user);
			return "redirect:/index.html";
	}



}
