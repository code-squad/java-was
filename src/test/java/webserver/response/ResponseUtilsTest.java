package webserver.response;

import org.junit.Test;
import webserver.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ResponseUtilsTest {

    @Test
    public void formatResponseLine() {
        String responseLine = ResponseUtils.formatResponseLine("HTTP/1.1", HttpStatus.OK);
        assertThat(responseLine, is("HTTP/1.1 200 OK \r\n"));
    }
}