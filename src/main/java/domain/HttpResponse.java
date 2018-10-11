package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private HttpStatusCode status;

    private String contentType = "text/html";
    private byte[] body;
    private DataOutputStream dos;
    // TODO cookies 객체로 뽑기
    private Map<String, String> cookies = new HashMap<>();

    public HttpResponse(DataOutputStream dos, HttpStatusCode status) {
        this.dos = dos;
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public HttpResponse response200Header(int lengthOfBodyContent, Cookies cookies) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");

            if (!contentType.equals("text/css")) {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");

            if (!cookies.isEmpty()) {
                dos.writeBytes("Set-Cookie: " + cookies.toString() + " Path=/" + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return this;
    }

    public HttpResponse response302Header(String url, Cookies cookies) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            if (!cookies.isEmpty()) {
                dos.writeBytes("Set-Cookie: " + cookies.toString() + " Path=/" + "\r\n");
            }
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return this;
    }

    public HttpResponse responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return this;
    }

    public boolean matchStatusCode(HttpStatusCode status) {
        return this.status == status;
    }
}
