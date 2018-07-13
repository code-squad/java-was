package webserver;

import org.junit.Test;

import java.io.DataOutputStream;

import static org.junit.Assert.*;

public class ResponseHeadersTest {
    private ResponseHeaders httpHeaders;
    private final DataOutputStream dos = new DataOutputStream(System.out);

    @Test
    public void writeHeaders_200() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html; charset=utf-8");
        ResponseBody body = new ResponseBody(Resource.of("/index.html"));

        httpHeaders = new ResponseHeaders(headers);
        httpHeaders.writeHeaders(dos, body);
    }

    @Test
    public void writeHeaders_302() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader(HttpHeader.LOCATION, "/index.html");
        ResponseBody body = new ResponseBody(Resource.ofEmpty());

        httpHeaders = new ResponseHeaders(headers);
        httpHeaders.writeHeaders(dos, body);
    }
}