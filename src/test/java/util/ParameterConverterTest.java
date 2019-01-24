package util;

import org.junit.Test;
import org.slf4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ParameterConverterTest {

    private static final Logger logger = getLogger(ParameterConverterTest.class);

    @Test
    public void 베이스64인코딩() {
        assertThat(ParameterConverter.urlDecoding("%EB%B0%95%EC%9E%AC%EC%84%B1")).isEqualTo("박재성");
        assertThat(ParameterConverter.urlDecoding("%EB%B0%95%EC%9E%AC%EC%84%B1%")).isEqualTo("%EB%B0%95%EC%9E%AC%EC%84%B1%");
    }

}
