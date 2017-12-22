package util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SplitUtilsTest {
	@Test
	public void getSplitedValueTest1() {
		String value = "hello/good.html";
		assertEquals("hello/good", SplitUtils.getSplitedValue(value, "\\.", 0));
		assertEquals("html", SplitUtils.getSplitedValue(value, "\\.", 1));
		assertEquals("hello", SplitUtils.getSplitedValue(value, "/", 0));
		assertEquals("good.html", SplitUtils.getSplitedValue(value, "/", 1));
	}
	@Test
	public void getSplitedValueTest2() {
		String value = "hello/good/html/form";
		assertEquals("hello", SplitUtils.getSplitedValue(value, "/", 0));
		assertEquals("good", SplitUtils.getSplitedValue(value, "/", 1));
		assertEquals("html", SplitUtils.getSplitedValue(value, "/", 2));
		assertEquals("form", SplitUtils.getSplitedValue(value, "/", 3));
	}
	
	@Test
	public void getSplitedBackValueTest1() {
		String value = "/user/create?UserId=qwer&password=1234";
		assertEquals("user/create?UserId=qwer&password=1234", SplitUtils.getSplitedBackValue(value, "/", 0));
		assertEquals("create?UserId=qwer&password=1234", SplitUtils.getSplitedBackValue(value, "/", 1));
	}
	
	@Test
	public void getSplitedBackValueTest2() {
		String value = "user/create?UserId=qwer&password=1234";
		assertEquals("create?UserId=qwer&password=1234", SplitUtils.getSplitedBackValue(value, "/", 0));
		assertEquals("", SplitUtils.getSplitedBackValue(value, "/", 1));
		assertEquals("", SplitUtils.getSplitedBackValue(value, "/", 2));
	}
	
	@Test
	public void getSplitedExtensionTest() {
		String value = "/user/form.html";
		assertEquals("html", SplitUtils.getSplitedExtension(value));		
	}
}