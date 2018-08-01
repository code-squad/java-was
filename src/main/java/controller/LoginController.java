package controller;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Autowired;
import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import db.DataBase;
import model.HttpResponse;
import model.User;

@Controller
@RequestMapping("/user/login")
public class LoginController  {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private DataBase database;
	
	@RequestMapping("")
	public String login(@RequestParam("userId") String userId, @RequestParam("password") String password, HttpResponse response) throws IOException {
		
		log.debug("userId : {} , password :{}", userId, password);
		User user = database.findUserById(userId);
		if (user == null) {
			log.debug("User Not Found!");
			return "redirect:/user/login_failed.html";
		}

		if (user.getPassword().equals(password)) {
			log.debug("login success!!");
			response.addHeader("Set-Cookie", "logined=true");
			return "redirect:/index.html";
		}
		log.debug("password mismatch!!");
		return "redirect:/user/login_failed.html";
	}


}
