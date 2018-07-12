package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private final StatusLine statusLine;
    private final ResponseHeaders headers;
    private final ResponseBody body;

    public HttpResponse(HttpStatus status, HttpHeaders headers, Resource resource) {
        this.statusLine = new StatusLine(status);
        this.headers = new ResponseHeaders(headers);
        this.body = new ResponseBody(resource);
    }

    public void writeResponse(OutputStream out) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            statusLine.writeStatusLine(dos);
            headers.writeHeaders(dos, body);
            body.writeBody(dos);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
