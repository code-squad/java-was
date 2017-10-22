package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private String header;
    private DataOutputStream dos;
    private Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }

    public void responseBody(byte[] bytes) {
        try {
            this.dos.writeBytes(header);
            log.debug(header);
            this.dos.writeBytes("\r\n");
            this.dos.write(bytes);
            log.debug(dos.toString());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    
}
