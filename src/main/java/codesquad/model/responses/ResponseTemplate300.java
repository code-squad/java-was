package codesquad.model.responses;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseTemplate300 implements ResponseTemplate {
    private static final Logger log = getLogger(ResponseTemplate300.class);

    @Override
    public void header(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes(response.writeLocation());
            dos.writeBytes(response.writeCookie());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void body(DataOutputStream dos, Response response) {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
