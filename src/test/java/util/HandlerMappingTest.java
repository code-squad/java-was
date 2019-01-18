package util;

import model.URLInfo;
import org.junit.Test;
import org.slf4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class HandlerMappingTest {

    private static final Logger logger = getLogger(HandlerMappingTest.class);

    @Test
    public void saveDataTest() {
        String path = "/create?name=doby";
        Object user = HandlerMapping.saveData(new URLInfo(path, "GET"));
        logger.debug(user.toString());
    }

    @Test
    public void obtainFieldTest() {
        assertThat(HandlerMapping.obtainField("setName")).isEqualTo("name");
    }

}
