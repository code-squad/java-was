package codesquad.model.responses;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseTemplate200 implements ResponseTemplate {
    private static final Logger log = getLogger(ResponseTemplate200.class);

    @Override
    public void header(DataOutputStream dos, Response response) {
        try {
            response.writeBody();
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(response.writeContentType());
            dos.writeBytes(response.writeCookie());
            dos.writeBytes(response.writeContentLength());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void body(DataOutputStream dos, Response response) {
        try {
            response.write(dos);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
