package controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import db.DataBase;
import dto.BoardDto;

public class BoardControllerTest {

	private BoardController boardController = new BoardController();

	private DataBase database;
	
	@Before
	public void setUp() {
		database = new DataBase();
	}
	
	@Test
	public void create() throws Exception {
		assertThat(boardController.create(new BoardDto(1L, "제목", "내용", "포비")), is("redirect:/index.html"));
		assertThat(database.findAllBoards().isEmpty(), is(false));
	}

	@Test
	public void delete() throws Exception {
		boardController.create(new BoardDto(2L, "제목", "내용", "포비"));
		boardController.delete(2L);
		assertNull(database.findBoardById(2L));
	}

}
