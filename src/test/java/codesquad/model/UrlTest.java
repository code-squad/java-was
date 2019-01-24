package codesquad.model;

import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import static codesquad.fixture.UrlFixture.URL;

public class UrlTest {
    private static final Logger log = getLogger(UrlTest.class);

    @Test
    public void of() {
        String urlText = "GET /user/form.html HTTP/1.1";
//        Url url = Url.of(urlText);
//        assertThat(url.getAccessPath(), is("/user/form.html"));
    }

    @Test
    public void parseUrl() {
        String urlText = "GET /user/create?userId=asdfasf&password=sadfasdf&name=afasdf&email=safa%40dsafa.com HTTP/1.1";
//        Url url = Url.of(urlText);
//        assertThat(url.getAccessPath(), is("/user/create"));
    }

    @Test
    public void hasSameFieldName_통과() {
        String[] fieldNames = {"userId", "password", "name", "email"};
        for (String fieldName : fieldNames) {
//            assertThat(URL.hasSameFieldName(fieldName), is(true));
        }
    }

    @Test
    public void renewAccessPath() {
        URL.renewAccessPath("/index.html");
        assertThat(URL.getAccessPath(), is("/index.html"));
    }
}