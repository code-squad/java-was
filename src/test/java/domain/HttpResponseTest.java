package domain;

import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpResponseTest {
    @Test
    public void canCreate() {
        HttpResponse response = new HttpResponse(null, null);
    }

    @Test
    public void getHttpStatusCode() {
        HttpResponse response = new HttpResponse(null, HttpStatusCode.OK);
        assertThat(response.getStatus(), is(HttpStatusCode.OK));
    }

    @Test
    public void matchStatusCode() {
        HttpResponse response = new HttpResponse(null, HttpStatusCode.FOUND);
        assertThat(response.matchStatusCode(HttpStatusCode.FOUND), is(true));
    }
}
