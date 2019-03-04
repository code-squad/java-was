package webserver;

import model.HttpMethod;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "HTTP_GET.txt"));
        HttpRequest request = new HttpRequest(in);

        assertThat(HttpMethod.GET).isEqualTo(request.getMethod());
        assertThat("/user/create").isEqualTo(request.getPath());
        assertThat("keep-alive").isEqualTo(request.getHeader("Connection"));
        assertThat("javajigi").isEqualTo(request.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "HTTP_POST.txt"));
        HttpRequest request = new HttpRequest(in);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getParameter("userId")).isEqualTo("javajigi");
    }
}