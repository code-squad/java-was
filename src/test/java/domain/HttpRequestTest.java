package domain;

import org.junit.Test;

public class HttpRequestTest {
    @Test
    public void canCreate() {
        HttpRequest request = new HttpRequest(null, null, null, null, null);
    }
}
