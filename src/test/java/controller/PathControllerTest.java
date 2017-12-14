package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.RequestPath;

public class PathControllerTest {
	PathController pathController = new PathController();

	@Test
	public void getFinalUrlTest() {
		RequestPath path1 = new RequestPath("hello.html");
//		html파일은 그 경로 그대로 출력
		assertEquals("hello.html", pathController.changeFinalUrl(path1));
//		일단 아닐 경우 / 루트로 보냄
		RequestPath path2 = new RequestPath("http://localhost:8080/user/create?userId=abcshc&password=1234&name=%EC%86%8C%ED%9D%AC%EC%B2%A0&email=thrrk%40naver.com");
		assertEquals("/", pathController.changeFinalUrl(path2));
	}
}
