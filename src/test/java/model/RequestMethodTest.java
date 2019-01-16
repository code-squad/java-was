package model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestMethodTest {

    @Test
    public void hasRequestMethod_GET() {
        assertThat(RequestMethod.isRequestMethod("GET")).isTrue();
    }

    @Test
    public void hasRequestMethod_POST() {
        assertThat(RequestMethod.isRequestMethod("POST")).isTrue();
    }

    @Test
    public void hasRequestMethod_invalid() {
        assertThat(RequestMethod.isRequestMethod("brad")).isFalse();
    }
}