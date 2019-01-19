package codesquad.webserver;

import codesquad.model.User;
import org.junit.Test;
import org.slf4j.Logger;

import static codesquad.fixture.UrlFixture.URL;
import static codesquad.fixture.UserFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinderTest {
    private static final Logger log = getLogger(ParameterBinderTest.class);

    @Test
    public void isValid() throws NoSuchMethodException {
        Class<User> targetClass = User.class;
        assertThat(ParameterBinder.isValid(targetClass, URL)).isTrue();
    }

    @Test
    public void binding() throws Exception {
        Class<?> parameterType = User.class;
        Object aInstance = parameterType.newInstance();
        assertThat(ParameterBinder.binding(aInstance, URL)).isEqualTo(USER);
    }
}