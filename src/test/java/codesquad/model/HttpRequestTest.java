package codesquad.model;

import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpRequestTest {
    private static final Logger log = getLogger(HttpRequestTest.class);

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        HttpRequest request = new HttpRequest(in);
    }

}