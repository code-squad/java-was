package domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseTest {
    @Test
    public void canCreate() {
        HttpResponse response = new HttpResponse(null);
    }

    @Test
    public void getHttpStatusCode() {
        HttpResponse response = new HttpResponse(HttpStatusCode.OK);
        assertThat(response.getStatus(), is(HttpStatusCode.OK));

    }
}
