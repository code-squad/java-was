package util;

import model.RequestEntity;
import model.User;
import org.junit.Test;
import org.slf4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class HandlerMappingTest {

    private static final Logger logger = getLogger(HandlerMappingTest.class);

    @Test
    public void saveDataTest() {
        String path = "/users/create?name=doby&userId=lee&password=password&email=lkhlkh09@gmail.com";
        Object user = HandlerMapping.saveData(new RequestEntity(path, "GET", null));
        assertThat(user).isEqualTo(new User("lee", "password", "doby", "lkhlkh09@gmail.com"));
    }

    @Test
    public void obtainFieldTest() {
        assertThat(HandlerMapping.obtainField("setName")).isEqualTo("name");
    }

}
