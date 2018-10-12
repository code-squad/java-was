package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private DataOutputStream dos;
    private Cookies cookies;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public HttpResponse response200Header(int lengthOfBodyContent) {
        return response200Header(lengthOfBodyContent, DEFAULT_CONTENT_TYPE);
    }

    public HttpResponse response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            log.debug("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");

            if (cookies != null) {
                dos.writeBytes("Set-Cookie: " + cookies.toString() + "; Path=/" + "\r\n");
            }

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

    public void sendRedirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            if (cookies != null) {
                log.debug("Set-Cookie: {}", cookies.toString());
                dos.writeBytes("Set-Cookie: " + cookies.toString() + "; Path=/" + "\r\n");
            }
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addCookie(String key, String value) {
        if (cookies == null) {
            cookies = new Cookies(new HashMap<>());
        }
        cookies.add(key, value);
        log.debug("Cookie Data : {}", cookies.toString());
    }
}
