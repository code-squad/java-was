package codesquad.webserver;

import codesquad.model.Header;
import codesquad.model.HttpSession;
import codesquad.model.User;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Map;

import static codesquad.fixture.UrlFixture.*;
import static codesquad.fixture.UserFixture.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinderTest {
    private static final Logger log = getLogger(ParameterBinderTest.class);

    @Test
    public void isValidForQuery_모든필드값_있을때() {
        Class<User> targetClass = User.class;
        Header header = new Header(URL, Maps.newHashMap());
        assertThat(ParameterBinder.isValidForQuery(targetClass, header)).isTrue();
    }

    @Test
    public void isValidForQuery_일부필드값_있을때() {
        Class<User> targetClass = User.class;
        Header header = new Header(URL2, Maps.newHashMap());
        assertThat(ParameterBinder.isValidForQuery(targetClass, header)).isTrue();
    }

    @Test
    public void isValidForQuery_일부필드값_틀릴때() {
        Class<User> targetClass = User.class;
        Header header = new Header(URL3, Maps.newHashMap());
        log.debug(header.toString());
        assertThat(ParameterBinder.isValidForQuery(targetClass, header)).isFalse();
    }

    @Test
    public void bindingQuery() throws Exception {
        Class<?> parameterType = User.class;
        Header header = new Header(URL, Maps.newHashMap());
        Object aInstance = parameterType.newInstance();
        assertThat(ParameterBinder.bindingQuery(aInstance, header)).isEqualTo(USER);
    }

    @Test
    public void isValidForCookie() {
        Object aInstance = new HttpSession();
        assertThat(ParameterBinder.isValidForCookie(aInstance)).isTrue();
    }

    @Test
    public void bindingCookie() throws Exception {
        Class<?> parameterType = HttpSession.class;
        Object aInstance = parameterType.newInstance();
        Map<String, String> map = Maps.newHashMap();
        map.put("Cookie", "logined=true");
        Header header = new Header(URL, map);
        ParameterBinder.bindingCookie(aInstance, header);
        HttpSession httpSession = (HttpSession) aInstance;
        assertThat(httpSession.getAttribute("logined")).isEqualTo("true");
    }
}