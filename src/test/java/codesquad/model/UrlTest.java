package codesquad.model;

import codesquad.util.HttpRequestUtils;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Map;

import static codesquad.fixture.UrlFixture.URL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class UrlTest {
    private static final Logger log = getLogger(UrlTest.class);

    @Test
    public void of() {
        String firstLine = "GET /user/form.html HTTP/1.1";
        Map<HttpRequestKey, String> parseFirstLine = HttpRequestUtils.parseFirstLine(firstLine);
        assertThat(parseFirstLine.get(HttpRequestKey.ACCESS_PATH), is("/user/form.html"));
    }

    @Test
    public void parseUrl() {
        String firstLine = "GET /user/create?userId=asdfasf&password=sadfasdf&name=afasdf&email=safa%40dsafa.com HTTP/1.1";
        Map<HttpRequestKey, String> parseFirstLine = HttpRequestUtils.parseFirstLine(firstLine);
        assertThat(parseFirstLine.get(HttpRequestKey.ACCESS_PATH), is("/user/create"));
        assertThat(parseFirstLine.get(HttpRequestKey.QUERY_VALUE), is("userId=asdfasf&password=sadfasdf&name=afasdf&email=safa%40dsafa.com"));
    }

    @Test
    public void renewAccessPath() {
        URL.renewAccessPath("/index.html");
        assertThat(URL.getAccessPath(), is("/index.html"));
    }
}