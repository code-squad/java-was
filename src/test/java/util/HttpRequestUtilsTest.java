package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import util.HttpRequestUtils.Pair;

public class HttpRequestUtilsTest {
	
	@Test
	public void parseUrl() {
		String httpHeader = "GET /user/login.html HTTP/1.1";
		assertThat(HttpRequestUtils.parseUrl(httpHeader), is("/user/login.html"));
	}
	
	@Test
	public void parseMethod() {
		String httpHeader = "GET /user/login.html HTTP/1.1";
		assertThat(HttpRequestUtils.parseMethod(httpHeader), is("GET"));
	}

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
	public void parseQueryStringnull() {
		Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
		assertThat(parameters.isEmpty(), is(true));

		parameters = HttpRequestUtils.parseQueryString("");
		assertThat(parameters.isEmpty(), is(true));

		parameters = HttpRequestUtils.parseQueryString(" ");
		assertThat(parameters.isEmpty(), is(true));
	}

	@Test
	public void parseQueryStringinvalid() {
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
}
