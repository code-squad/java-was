package codesquad.model.responses;

import codesquad.model.Header;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseTemplate300 implements ResponseTemplate {
    private static final Logger log = getLogger(ResponseTemplate300.class);

    @Override
    public byte[] header(DataOutputStream dos, Header header) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + header.generateAccessPath() + "\r\n");
            if(header.hasCookie()) {
                dos.writeBytes(header.writeCookie() + "\r\n");
                log.debug("쿠키값 입력 : " + header.writeCookie());
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void body(DataOutputStream dos, byte[] body) {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
