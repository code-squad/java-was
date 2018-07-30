package webserver;

import org.junit.Test;

import java.io.DataOutputStream;

import static org.junit.Assert.*;

public class HttpResponseTest {
    private HttpResponse response;
    private final DataOutputStream dos = new DataOutputStream(System.out);

    @Test
    public void writeResponse_200() {
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html; charset=utf-8");
        headers.addHeader(HttpHeader.CONTENT_LENGTH, "");
        response = new HttpResponse(HttpStatus.OK, headers, Resource.of("/index.html"));
        response.writeResponse(dos);
    }

    @Test
    public void writeResponse_302() {
        HttpHeaders headers = new HttpHeaders();
        headers.addHeader(HttpHeader.LOCATION, "/index.html");
        response = new HttpResponse(HttpStatus.FOUND, headers, Resource.ofEmpty());
        response.writeResponse(dos);
    }
}