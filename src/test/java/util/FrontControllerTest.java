package util;

import org.junit.Test;
import com.larry.webserver.FrontController;
import com.larry.webserver.HttpStatus;
import com.larry.webserver.Request;
import com.larry.webserver.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static com.larry.webserver.Response.DOMAIN;

public class FrontControllerTest {

    @Test
    public void getResponse_get() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/chaegyunjung/Documents/codesquad/java-was/http-requests/indexGet.http"));
        Request request = new Request(br);

        FrontController frontController = new FrontController();
        Response response = frontController.resolveRequest(request);

        byte[] body = Files.readAllBytes(new File("webapp/" + "index.html").toPath());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8\r\n");
        headers.put("Content-Length", String.valueOf(body.length));

        assertThat(response.getHttpVersion(), is("HTTP/1.1"));
        assertThat(response.getHTTP_STATUS(), is(HttpStatus.OK));
        assertThat(response.getHeaders(), is(headers));
    }

    @Test
    public void getResponse_post() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/chaegyunjung/Documents/codesquad/java-was/http-requests/userCreatePost.http"));
        Request request = new Request(br);

        FrontController frontController = new FrontController();
        Response response = frontController.resolveRequest(request);

        byte[] body = Files.readAllBytes(new File("webapp/" + "index.html").toPath());
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", DOMAIN+"/" + "index.html");
        headers.put("Content-Type", "text/html;charset=utf-8\r\n");
        headers.put("Content-Length", String.valueOf(body.length));

        assertThat(response.getHttpVersion(), is("HTTP/1.1"));
        assertThat(response.getHTTP_STATUS(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders(), is(headers));

    }
}
