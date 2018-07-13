package webserver;

import org.junit.Test;

import java.io.DataOutputStream;

import static org.junit.Assert.*;

public class ResponseBodyTest {
    private ResponseBody body;
    private final DataOutputStream dos = new DataOutputStream(System.out);

    @Test
    public void writeBody() throws Exception {
        body = new ResponseBody(Resource.of("/index.html"));
        body.writeBody(dos);
    }

    @Test
    public void writeBody_No_Content() throws Exception {
        body = new ResponseBody(Resource.ofEmpty());
        body.writeBody(dos);
    }
}