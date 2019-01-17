package model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UrlTest {

    @Test
    public void parseUrl() {
        String urlText = "/user/create?userId=asdfasf&password=sadfasdf&name=afasdf&email=safa%40dsafa.com";
        Url url = Url.of(urlText);
        assertThat(url.generate(), is("./webapp/user/create"));
    }

    @Test
    public void parseUrl_queryString없을때() {
        String urlText = "/user/index.html";
        Url url = Url.of(urlText);
        assertThat(url.generate(), is(Url.ROOT_STATIC_PATH + urlText));
    }

}