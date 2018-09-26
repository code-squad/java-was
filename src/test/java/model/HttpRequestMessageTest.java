package model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpRequestMessageTest {
    @Test
    public void getUrl() {
        String url = "";
        HttpRequestMessage requestMessage = new HttpRequestMessage(url);
        assertThat(requestMessage.getUrl(), is(url));
    }

    @Test
    public void getPath() {

    }
}
