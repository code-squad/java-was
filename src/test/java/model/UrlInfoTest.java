package model;

import org.junit.Test;
import org.slf4j.Logger;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class UrlInfoTest {

    private static final Logger logger = getLogger(UrlInfoTest.class);

    private static final String path
            = "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

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

    @Test
    public void urlInfo생성자_적용_테스트() {
        assertThat(new URLInfo(path, "GET")).isEqualTo(new URLInfo("/user/create", "GET"));
    }

    @Test
    public void obtainParameterTest() {
        URLInfo urlInfo = new URLInfo(path, "GET");
        assertThat(urlInfo.obtainParamElement("userId")).isEqualTo("javajigi");
        assertThat(urlInfo.obtainParamElement("blank")).isEqualTo("");
    }

    @Test
    public void obtainURLTest() {
        assertThat(URLInfo.obtainURL("GET /user/create?userId=javajigi HTTP/1.1"))
                .isEqualTo("/user/create?userId=javajigi");

    }
}
