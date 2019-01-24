package util;

import model.RequestEntity;
import model.User;
import org.junit.Test;
import org.slf4j.Logger;
import webserver.HandlerMapping;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class HandlerMappingTest {

    private static final Logger logger = getLogger(HandlerMappingTest.class);

    private RequestEntity requestEntity = new RequestEntity("/users/login", "POST"
            , "userId=doby&password=password&name=LEEKIHYUN&email=lkhlkh09@gmail.com", null);

    @Test
    public void obtainFieldTest() {
        assertThat(HandlerMapping.obtainField("setName")).isEqualTo("name");
    }

    @Test
    public void readUserId() {
        assertThat(HandlerMapping.readParameter(requestEntity, "userId")).isEqualTo("doby");
    }

    @Test
    public void createObject_문자열_Test() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "doby");
    }

}
