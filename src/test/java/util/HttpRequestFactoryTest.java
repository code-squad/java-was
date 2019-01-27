package util;

import model.HttpRequest;
import org.junit.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestFactoryTest {

    @Test
    public void of_Get_QueryString_Create_Test() throws IOException {
        String path = new File(".").getCanonicalPath();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(path + "/src/test/java/resource/HttpRequestGetHeader.txt"))));

        HttpRequest httpRequest = HttpRequestFactory.of(br);
        assertThat(httpRequest.obtainHeader("Accept")).isEqualTo("*/*");
        assertThat(httpRequest.getBody().get("userId")).isEqualTo("javajigi");
    }
}
