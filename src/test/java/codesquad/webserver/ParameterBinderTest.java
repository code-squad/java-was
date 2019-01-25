package codesquad.webserver;

import codesquad.model.HttpSession;
import codesquad.model.User;
import codesquad.model.request.HttpRequest;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static codesquad.fixture.UserFixture.USER;
import static codesquad.model.HttpRequestTest.TEST_DIRECTORY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinderTest {
    private static final Logger log = getLogger(ParameterBinderTest.class);

    @Test
    public void MethodParameter_COMMON() throws Exception {
        Class<?> parameterType = User.class;
        Object aInstacne = parameterType.newInstance();
        assertThat(MethodParameter.getMethodParameter(aInstacne)).isEqualTo(MethodParameter.COMMON);
    }

    @Test
    public void MethodParameter_HTTP_SESSION() throws Exception {
        Class<?> parameterType = HttpSession.class;
        Object aInstacne = parameterType.newInstance();
        assertThat(MethodParameter.getMethodParameter(aInstacne)).isEqualTo(MethodParameter.HTTP_SESSION);
    }

    @Test
    public void isValidForQuery_모든필드값_있을때() throws Exception {
        Class<?> targetClass = User.class;
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_POST.txt"));
        HttpRequest httpRequest = new HttpRequest(in);
        HandlerMethodArgumentResolver handlerMethodArgumentResolver = new QueryHandlerMethodArgumentResolver();
        assertThat(handlerMethodArgumentResolver.supportsParameter(targetClass, httpRequest)).isTrue();
    }

    @Test
    public void isValidForQuery_일부필드값_있을때() throws Exception {
        Class<?> targetClass = User.class;
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_POST2.txt"));
        HttpRequest httpRequest = new HttpRequest(in);
        HandlerMethodArgumentResolver handlerMethodArgumentResolver = new QueryHandlerMethodArgumentResolver();
        assertThat(handlerMethodArgumentResolver.supportsParameter(targetClass, httpRequest)).isTrue();
    }

    @Test
    public void isValidForQuery_일부필드값_틀릴때() throws Exception {
        Class<?> targetClass = User.class;
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_POST3.txt"));
        HttpRequest httpRequest = new HttpRequest(in);
        HandlerMethodArgumentResolver handlerMethodArgumentResolver = new QueryHandlerMethodArgumentResolver();
        assertThat(handlerMethodArgumentResolver.supportsParameter(targetClass, httpRequest)).isFalse();
    }

    @Test
    public void bindingQuery() throws Exception {
        Class<?> parameterType = User.class;
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_POST.txt"));
        HttpRequest httpRequest = new HttpRequest(in);
        Object aInstance = parameterType.newInstance();
        HandlerMethodArgumentResolver handlerMethodArgumentResolver = new QueryHandlerMethodArgumentResolver();
        assertThat(handlerMethodArgumentResolver.resolveArgument(aInstance, httpRequest)).isEqualTo(USER);
    }

    @Test
    public void isValidForCookie_세션값없을때() throws Exception {
        Class<?> targetClass = HttpSession.class;
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_POST.txt"));
        HttpRequest httpRequest = new HttpRequest(in);
        HandlerMethodArgumentResolver handlerMethodArgumentResolver = new SessionHandlerMethodArgumentResovler();
        assertThat(handlerMethodArgumentResolver.supportsParameter(targetClass, httpRequest)).isTrue();
    }
}