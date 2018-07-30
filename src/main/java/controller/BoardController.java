package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Autowired;
import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import dto.BoardDto;

@Controller
@RequestMapping("/boards")
public class BoardController {
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private DataBase database;
	
	@RequestMapping("")
	public String create(BoardDto boardDto) {
			log.debug("BoardDto :{}", boardDto.toString());
			database.addBoard(boardDto.toBoard());
			return "redirect:/index.html";
	}
	
	@RequestMapping("/delete")
	public String delete(Long id) {
		database.deleteBoard(id);
		return "redirect:/index.html";
	}
	

}
