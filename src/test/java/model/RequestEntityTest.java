package model;

import org.junit.Test;
import org.slf4j.Logger;
import util.HttpRequestUtils;

import java.util.Iterator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class RequestEntityTest {

    private static final Logger logger = getLogger(RequestEntityTest.class);

    private static final String path
            = "/user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

    @Test
    public void obtainURL() {
        assertThat(RequestEntity.obtainURL("GET /api/url HTTP1.1")).isEqualTo("/api/url");
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
    public void obtainParameterTest() {
        RequestEntity requestEntity = new RequestEntity(path, "GET", "");
        assertThat(requestEntity.obtainParamElement("userId")).isEqualTo("javajigi");
        assertThat(requestEntity.obtainParamElement("blank")).isEqualTo("");
    }

    @Test
    public void obtainURLTest() {
        assertThat(RequestEntity.obtainURL("GET /user/create?userId=javajigi HTTP/1.1"))
                .isEqualTo("/user/create?userId=javajigi");

    }
}
