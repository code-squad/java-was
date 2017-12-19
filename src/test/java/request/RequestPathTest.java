package request;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import request.RequestPath;

public class RequestPathTest {
	RequestPath requestPath;
	
	@Before
	public void setPath() {
		requestPath = new RequestPath("/user/create?userId=abcshc&password=1234&name=%EC%86%8C%ED%9D%AC%EC%B2%A0&email=thrrk%40naver.com");
	}
	
	@Test
	public void pathOnlyUrlTest() {
		assertEquals("/user/create", requestPath.getOnlyUrl());
	}
	
	@Test
	public void getInputValueTest() {
		Map<String, String> inputValue = requestPath.getInputValue();
		assertEquals("abcshc", inputValue.get("userId"));
		assertEquals("1234", inputValue.get("password"));
		assertEquals("%EC%86%8C%ED%9D%AC%EC%B2%A0", inputValue.get("name"));
		assertEquals("thrrk%40naver.com", inputValue.get("email"));
	}
}