package util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import static org.junit.Assert.assertThat;
public class StringUtilsTest {
	StringUtils su = new StringUtils();
	@Test
	public void 경로추출테스트() {
		String line = "GET /index.html HTTP/1.1";
		String[] tokens = line.split(" ");
		String answer = su.generatePath(tokens); 
		assertThat(answer,is("/index.html"));
	}
	
	@Test
	public void 유저생성확인() {
		String url = "/user/create?userId=javajigi&password=password&name=포비&email=javajigi%40slipp.net";
		assertEquals(su.checkCreateUser(url),true);
	}

}
