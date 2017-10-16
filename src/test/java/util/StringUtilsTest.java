package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void test() {
		assertEquals("hi", StringUtils.parseQueryString("asdjfla;slkdjfl;?hi"));
	}

}
