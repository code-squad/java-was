package model;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpRequestMessageTest {
    
    private static final Logger log =  LoggerFactory.getLogger(HttpRequestMessageTest.class);
    
//    @Test
//    public void getUrl() {
//        String url = "";
//        HttpRequestMessage requestMessage = new HttpRequestMessage(url);
//        assertThat(requestMessage.getUrl(), is(url));
//    }

    @Test
    public void getPath() {
        String url = "/user/create?userId=david";
        String path = url.substring(0, url.lastIndexOf("?"));

        HttpRequestMessage requestMessage = new HttpRequestMessage(path, 0);

        assertThat(requestMessage.getPath(),is("/user/create"));
    }

    @Test
    public void getContentLength() {
        String content = "hello";
        HttpRequestMessage requestMessage = new HttpRequestMessage(null, content.length());

        assertThat(requestMessage.getContentLength(), is(5));
    }
}
