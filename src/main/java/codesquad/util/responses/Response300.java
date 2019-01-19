package codesquad.util.responses;

import codesquad.model.Url;
import codesquad.webserver.WebServer;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Response300 implements Response {
    private static final Logger log = getLogger(Response300.class);

    @Override
    public void header(DataOutputStream dos, Url url) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:" + WebServer.DEFAULT_PORT + url.getAccessPath() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void body(DataOutputStream dos) {
        try {
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
