package domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpRequestTest {
    @Test
    public void canCreate() {
        HttpRequest request = new HttpRequest(null, null, null, null, null);
    }

    @Test
    public void getter() {
        HttpRequest request = new HttpRequest("GET", null, "/user/create", null, null);
        assertThat(request.getMethod(), is("GET"));
        assertThat(request.getMethod(), is("/user/create"));
    }
}
