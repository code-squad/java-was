package codesquad.model.responses;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseTemplate200 implements ResponseTemplate {
    private static final Logger log = getLogger(ResponseTemplate200.class);

    @Override
    public void header(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            httpResponse.writeBody();
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(httpResponse.writeContentType());
            dos.writeBytes(httpResponse.writeCookie());
            dos.writeBytes(httpResponse.writeContentLength());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void body(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            httpResponse.write(dos);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
