package webserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import db.DataBase;
import model.User;

public class RequestHandlerTest {
	private String testDirectory = "./src/test/resources/";

	@Test
	public void get_method_test() throws IOException {
//		InputStream stream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET_index.txt"));
		RequestHandler rqst = new RequestHandler();
		byte[] body = rqst.readInput(in);
		byte[] testBody = rqst.pathByteArray("/index.html");
		assertArrayEquals(testBody, body);
	}
	
	@Test
	public void createUser_test() throws UnsupportedEncodingException {
		RequestHandler rqst = new RequestHandler();
		rqst.createUser("userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net");
		User user = DataBase.findUserById("javajigi");
		assertThat(user.getName(), is("%EB%B0%95%EC%9E%AC%EC%84%B1"));
	}
}
