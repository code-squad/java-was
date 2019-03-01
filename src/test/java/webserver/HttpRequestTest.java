package webserver;

import model.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import util.HttpRequestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class HttpRequestTest {
    String requestLine;

    @Before
    public void setUp() throws Exception {
        requestLine = "GET /index.html HTTP/1.1";
    }

    @Test
    public void getHttpMethod() {
        HttpRequest request = new HttpRequest(requestLine);
        HttpMethod method = request.getHttpMethod();
       // assertThat(method).isEqualTo(new HttpMethod("GET"));
       // assertThat(method).isNotEqualTo(new HttpMethod("POST"));
    }

    @Test
    public void getHeader() {
        String requestString = "Content-Length: 59";
        HttpRequest request =  new HttpRequest(requestLine);
    }
}