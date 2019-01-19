package codesquad.util.responses;

import codesquad.model.Request;
import codesquad.model.Url;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.slf4j.LoggerFactory.getLogger;

public class Response200 implements Response {
    private static final Logger log = getLogger(Response200.class);

    private byte[] body;

    @Override
    public void header(DataOutputStream dos, Request request) {
        Url url = request.getUrl();
        try {
            body = Files.readAllBytes(new File(url.generateFilePath()).toPath());
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            if(request.hasCookieVal()) {
                dos.writeBytes("Set-Cookie: " + request.writeCookie());
                log.debug("cookie write : {}", request.writeCookie());
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
