package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import dto.UserDto;

@Controller
@RequestMapping("/create")
public class CreateUserController {
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

	@RequestMapping("")
	public String create(UserDto userDto) {
		
			log.debug("UserDto :{}", userDto.toString());
			DataBase.addUser(userDto.toUser());
			return "redirect:/index.html";
	}

}
