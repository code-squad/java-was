package codesquad.webserver;

import codesquad.Controller;
import codesquad.model.Header;
import codesquad.util.responses.ResponseCode;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.util.Set;

import static codesquad.fixture.UrlFixture.URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class MappingHandlerTest {
    private static final Logger log = getLogger(MappingHandlerTest.class);
    private Header header;

    @Before
    public void setUp() throws Exception {
        header = new Header();
        header.setUrl(URL);
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
        assertThat(header.getResponseCode()).isEqualTo(ResponseCode.FOUND);
    }

    @Test
    public void ok() {
        Object result = "/index.html";
        header.generateResponseCode(result);
        assertThat(header.getResponseCode()).isEqualTo(ResponseCode.OK);
    }
}