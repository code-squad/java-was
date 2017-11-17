package db;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;

public class DataBaseTest {
	private static final Logger logger = LoggerFactory.getLogger(DataBase.class);

	 @Test
	 public void addUserListTest() throws IOException {
	 DataBase.addUser(new User("ididid","passssss", "namename",
	 "email@email.com"));
	 byte[] body = Files.readAllBytes(new File("./webapp" +
	 "/user/list.html").toPath());
	 body = DataBase.addUserList(body);
	
	 logger.debug(new String(body));
	 assertNotNull(body);
	 }

}
