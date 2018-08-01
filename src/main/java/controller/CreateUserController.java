package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Autowired;
import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import dto.UserDto;

@Controller
@RequestMapping("/create")
public class CreateUserController {
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);
	
	@Autowired
	private DataBase database;
	
	@RequestMapping("")
	public String create(UserDto userDto) {
		
			log.debug("UserDto :{}", userDto.toString());
			database.addUser(userDto.toUser());
			return "redirect:/index.html";
	}

}
