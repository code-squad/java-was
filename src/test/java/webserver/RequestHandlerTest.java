package webserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		HttpRequest reqst = new HttpRequest(in);
		assertThat("/index.html", is(reqst.getURI()));
	}
	
	@Test
	public void createUser_get_test() throws IOException {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET_User.txt"));
		HttpRequest reqst = new HttpRequest(in);
		assertThat("/user/create", is(reqst.getURI()));
	}
	
	@Test
	public void createUser_post_test() throws IOException {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST_User.txt"));
		HttpRequest reqst = new HttpRequest(in);
		assertThat("/user/create", is(reqst.getURI()));
	}
}
