package codesquad.model;

import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpRequestTest {
    private static final Logger log = getLogger(HttpRequestTest.class);

    public static final String TEST_DIRECTORY = "./src/test/java/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);
        log.debug("request : {}", request);
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(TEST_DIRECTORY + "Http_POST.txt"));
        HttpRequest request = new HttpRequest(in);
        log.debug("request : {}", request);
    }
}