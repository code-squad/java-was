package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.RequestMapping;
import db.DataBase;
import dto.BoardDto;

@Controller
@RequestMapping("/boards")
public class BoardController {
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	@RequestMapping("")
	public String create(BoardDto boardDto) {
			log.debug("BoardDto :{}", boardDto.toString());
			DataBase.addBoard(boardDto.toBoard());
			return "redirect:/index.html";
	}
	
	@RequestMapping("/delete")
	public String delete(Long id) {
		DataBase.deleteBoard(id);
		return "redirect:/index.html";
	}
	

}
