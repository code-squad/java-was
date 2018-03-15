package webserver;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class HttpMethodTest {
    HttpMethod method;
    @Test
    public void isGet() {
        method = HttpMethod.GET;
        assertEquals(true, method.isGet());
    }

}
