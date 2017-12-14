package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import db.DataBase;
import model.RequestPath;
import model.User;

public class UserControllerTest {
	UserController userController = new UserController();
	RequestPath requestPath;
	
	@Before
	public void initRequestPath() {
		requestPath = new RequestPath("/user/create?userId=abcshc&password=1234&name=%EC%86%8C%ED%9D%AC%EC%B2%A0&email=thrrk%40naver.com");
		User user = userController.saveUser(requestPath.getInputValue());
		DataBase.addUser(user);
	}

	@Test
	public void addUserTest() {
		User findUser = DataBase.findUserById("abcshc");
		assertEquals("%EC%86%8C%ED%9D%AC%EC%B2%A0", findUser.getName());
		assertEquals("thrrk%40naver.com", findUser.getEmail());
		assertEquals("1234", findUser.getPassword());
		assertEquals("abcshc", findUser.getUserId());
	}

}
