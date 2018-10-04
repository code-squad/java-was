package domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpRequestTest {
    @Test
    public void canCreate() {
        HttpRequest request = new HttpRequest(null, null, null, null);
    }

    @Test
    public void getter() {
        HttpRequest request = new HttpRequest("GET", "/user/create", "userId=javajigi&password=password", null);
        assertThat(request.getMethod(), is("GET"));
        assertThat(request.getPath(), is("/user/create"));
        assertThat(request.getParameter(), is("userId=javajigi&password=password"));
        assertThat(request.getValueOfParameter("userId"), is("javajigi"));
    }
}
