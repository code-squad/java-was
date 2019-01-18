package model;

import org.junit.Test;
import org.slf4j.Logger;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class UrlInfoTest {

    private static final Logger logger = getLogger(UrlInfoTest.class);

    @Test
    public void obtainURL() {
        assertThat(URLInfo.obtainURL("GET /api/url HTTP1.1")).isEqualTo("/api/url");
    }

    @Test
    public void parseQueryString_테스트() {
        String param = "id=10002284&name=doby";
        Map<String, String> map = HttpRequestUtils.parseQueryString(param);
        Iterator<String> it = map.keySet().iterator();

        while(it.hasNext()) {
            String key = it.next();
            logger.debug("Key : {}, Value : {}", key, map.get(key));
        }
    }

    /*@Test(expected = IOException.class)
    public void obtainBody_존재하지않는파일_실패() throws IOException {
        IOUtils.obtainBody(new URLInfo("/api/users"));
    }

    @Test
    public void urlInfo생성자_적용_테스트() {
        URLInfo urlInfo = new URLInfo("/create?userId=javajigi&password=password&email=javajigi%40slipp.net");
        logger.debug("URLINFO : {}", urlInfo.toString());
    }*/
}
