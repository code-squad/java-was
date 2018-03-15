package Controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import webserver.RequestHandler;

public class CreateUserControllerTest {
	private static final Logger log = LoggerFactory.getLogger(CreateUserControllerTest.class);
	
	@Test
	public void createUser_test() {
		Map<String, String> param = new HashMap<>();
		param.put("userId", "javajigi");
		param.put("password", "password");
		param.put("name", "박재성");
		param.put("email", "javajigi%40slipp.net");
		
		try {
			CreateUserController cc = new CreateUserController();
			cc.createUser(param);
			assertThat("박재성", is(DataBase.findUserById("javajigi").getName()));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
