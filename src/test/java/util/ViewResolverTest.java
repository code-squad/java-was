package util;

import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ViewResolverTest {

    private static final Logger logger = getLogger(ViewResolverTest.class);

    @Test
    public void root_디렉토리_확인_Test() throws IOException {
        String path = new File(".").getCanonicalPath();
        logger.debug("Root Path : {}", path);
    }
}
