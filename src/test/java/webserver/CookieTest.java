package webserver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import model.Cookie;

public class CookieTest {

	@Test
	public void test() {
		Cookie cookie = new Cookie("logined=true");
		System.out.println(cookie.get("logined"));
		assertThat(cookie.get("logined"), is("true"));
	}

}
