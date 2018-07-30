package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHeaders {
    private final HttpHeaders headers;

    public ResponseHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void writeHeaders(DataOutputStream dos, ResponseBody body) throws IOException {
        if (body.exists()) {
            headers.addHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(body.getContentLength()));
        }
        dos.writeBytes(headers.buildHeaders());
        dos.writeBytes("\r\n");
    }
}
