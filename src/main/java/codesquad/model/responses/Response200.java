package codesquad.model.responses;

import codesquad.model.Header;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Response200 implements Response {
    private static final Logger log = getLogger(Response200.class);

    @Override
    public byte[] header(DataOutputStream dos, Header header) {
        byte[] body = null;
        try {
            body = header.writeBody();
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if(header.isCssFile()) {
                dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            }
            else {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }
            if(header.isCookieModified()) {
                dos.writeBytes(header.writeCookie());
            }
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return body;
    }

    @Override
    public void body(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
