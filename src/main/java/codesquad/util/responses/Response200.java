package codesquad.util.responses;

import codesquad.model.Header;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Response200 implements Response {
    private static final Logger log = getLogger(Response200.class);

    private byte[] body;

    @Override
    public void header(DataOutputStream dos, Header header) {
        try {
            body = header.writeBody();
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            if(header.isCookieModified()) {
                dos.writeBytes(header.writeCookie());
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void body(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
