package Controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class LoginControllerTest {
	private static final Logger log = LoggerFactory.getLogger(LoginControllerTest.class);
	
	@Before
	public void settting() {
		DataBase.addUser(new User("javajigi", "password", "박재성", "javajigi%40slipp.net"));
		log.debug("SETTING TEST DATA : " + DataBase.findUserById("javajigi"));
	}

	@Test
	public void createUser_test() {
		Map<String, String> param = new HashMap<>();
		param.put("userId", "javajigi");
		param.put("password", "password");
		param.put("name", "박재성");
		param.put("email", "javajigi%40slipp.net");

		try {
			LoginController loginController = new LoginController();
			assertThat(true, is(loginController.loginCheck(param)));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
