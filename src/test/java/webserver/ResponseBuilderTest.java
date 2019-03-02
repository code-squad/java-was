package webserver;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ResponseBuilderTest {

    @Test
    public void sb() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1");
        sb.append(" ");
        sb.append("200");
        sb.append(" ");
        sb.append("OK");

        assertThat(sb.toString()).isEqualTo("HTTP/1.1 200 OK");

    }
}