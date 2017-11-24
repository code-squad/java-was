package http;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CookieTest {
	Cookie cookie;

	@Before
	public void setup() {
		cookie = new Cookie("logined=true");
	}

	@Test
	public void getterTest() {
		assertEquals("true", cookie.get("logined"));
	}

	@Test
	public void putTest() {
		cookie.put("password", "qwer1234");
		assertEquals("qwer1234", cookie.get("password"));
	}

}
