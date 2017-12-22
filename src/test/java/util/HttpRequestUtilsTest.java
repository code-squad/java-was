package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import util.HttpRequestUtils.Pair;
import util.HttpRequestUtils.RequestMethodType;

public class HttpRequestUtilsTest {
	@Test
	public void parseQueryString() {
		String queryString = "userId=javajigi";
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
		assertThat(parameters.get("userId"), is("javajigi"));
		assertThat(parameters.get("password"), is(nullValue()));

		queryString = "userId=javajigi&password=password2";
		parameters = HttpRequestUtils.parseQueryString(queryString);
		assertThat(parameters.get("userId"), is("javajigi"));
		assertThat(parameters.get("password"), is("password2"));
	}

	@Test
	public void parseQueryStringNull() {
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
		assertThat(parameters.isEmpty(), is(true));

		parameters = HttpRequestUtils.parseQueryString("");
		assertThat(parameters.isEmpty(), is(true));

		parameters = HttpRequestUtils.parseQueryString(" ");
		assertThat(parameters.isEmpty(), is(true));
	}

	@Test
	public void parseQueryStringInvalid() {
		String queryString = "userId=javajigi&password";
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
		assertThat(parameters.get("userId"), is("javajigi"));
		assertThat(parameters.get("password"), is(nullValue()));
	}

	@Test
	public void parseCookies() {
		String cookies = "logined=true; JSessionId=1234";
		Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
		assertThat(parameters.get("logined"), is("true"));
		assertThat(parameters.get("JSessionId"), is("1234"));
		assertThat(parameters.get("session"), is(nullValue()));
	}

	@Test
	public void getKeyValue() throws Exception {
		Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
		assertThat(pair, is(new Pair("userId", "javajigi")));
	}

	@Test
	public void getKeyValueInvalid() throws Exception {
		Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
		assertThat(pair, is(nullValue()));
	}

	@Test
	public void parseHeader() throws Exception {
		String header = "Content-Length: 59";
		Pair pair = HttpRequestUtils.parseHeader(header);
		assertThat(pair, is(new Pair("Content-Length", "59")));
	}

	@Test
	public void methodTypeTest() {
		assertEquals(RequestMethodType.getRequestMethodType("GET"), RequestMethodType.GET);
		assertEquals(RequestMethodType.getRequestMethodType("get"), RequestMethodType.GET);
		assertEquals(RequestMethodType.getRequestMethodType("POST"), RequestMethodType.POST);
		assertEquals(RequestMethodType.getRequestMethodType("Post"), RequestMethodType.POST);
	}
}
