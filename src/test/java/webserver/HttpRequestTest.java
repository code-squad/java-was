package webserver;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class HttpRequestTest {

    private static final String TESTABLE_STRING = "HELLO POBI";

    private HttpRequest hr;
    private BufferedReader br;
    private InputStream is;

    @Before
    public void setup() throws Exception {
        this.is = new ByteArrayInputStream(TESTABLE_STRING.getBytes());
        StringBuilder sb = new StringBuilder();
        this.br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        this.hr = HttpRequest.parseHttpBodyFromInputStream(is, br, sb);
        hr.setRequestBody();
    }

    @Test
    public void test() throws IOException {
        System.out.println(this.br.readLine());
        assertEquals(TESTABLE_STRING, this.hr.getRequestBody());
    }

}
