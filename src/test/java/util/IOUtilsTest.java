package util;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class IOUtilsTest {
	private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

	@Test
	public void readData() throws Exception {
		String data = "abcd123";
		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);

		logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
	}

	@Test
	public void addUserListTest() throws IOException {
		DataBase.addUser(new User("ididid","passssss", "namename", "email@email.com"));
		byte[] body = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
		body = IOUtils.addUserList(body);
		
		logger.debug(new String(body));
		assertNotNull(body);
	}
}
