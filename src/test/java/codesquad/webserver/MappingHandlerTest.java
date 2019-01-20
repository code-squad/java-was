package codesquad.webserver;

import codesquad.Controller;
import codesquad.model.Header;
import codesquad.model.responses.Response;
import codesquad.model.responses.Response200;
import codesquad.model.responses.Response300;
import codesquad.model.responses.ResponseCode;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

import static codesquad.fixture.UrlFixture.URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class MappingHandlerTest {
    private static final Logger log = getLogger(MappingHandlerTest.class);
    private Header header;
    private static Map<ResponseCode, Response> responses = Maps.newHashMap();

    static {
        responses.put(ResponseCode.OK, new Response200());
        responses.put(ResponseCode.FOUND, new Response300());
    }

    @Before
    public void setUp() throws Exception {
        header = new Header(URL, Maps.newHashMap());

    }

    @Test
    public void reflectionTest() {
        Reflections reflections = new Reflections("codesquad");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : annotated) {
            log.debug(aClass.getName());
        }
    }

    @Test
    public void redirect() {
        Object result = "redirect:/index.html";
        header.generateResponseCode(result);
        Response response = header.getResponse(responses);
        assertThat(response instanceof Response300).isTrue();
    }

    @Test
    public void ok() {
        Object result = "/index.html";
        header.generateResponseCode(result);
        Response response = header.getResponse(responses);
        assertThat(response instanceof Response200).isTrue();
    }
}