package dto;

import org.slf4j.Logger;
import util.HttpResponseHeaderUtils;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseMessage {
    private static final Logger log = getLogger(ResponseMessage.class);

    private String header;
    private byte[] body;

    private ResponseMessage(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public static ResponseMessage ofDefault() {
        return ResponseMessage.ofBody("Hello World".getBytes());
    }

    public static ResponseMessage ofBody(byte[] body) {
        return new ResponseMessage(HttpResponseHeaderUtils.generate200Header(body.length), body);
    }

    public static ResponseMessage ofMessage(String header, byte[] body) {
        return new ResponseMessage(header, body);
    }

    public void response(DataOutputStream dos) {
        try {
            log.debug("responseHeader : {}", header);
            dos.writeBytes(header);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
