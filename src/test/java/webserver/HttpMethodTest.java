package webserver;

import exception.NotSupportedMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpMethodTest {

    @Test
    public void methodGet() {
        HttpMethod method = HttpMethod.get("GET");
        assertEquals(method, HttpMethod.GET);
    }

    @Test(expected = NotSupportedMethod.class)
    public void methodGet_err() {
        HttpMethod.get("COLIN");
    }
}