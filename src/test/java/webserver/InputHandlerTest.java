package webserver;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class InputHandlerTest {
	private String testDirectory = "./src/test/resources/";

	@Test
	public void get_method_test() throws IOException {
//		InputStream stream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET_index.txt"));
		byte[] body = InputHandler.doRequest(in);
		byte[] testBody = InputHandler.pathByteArray("/index.html");
		assertArrayEquals(testBody, body);
	}
}
