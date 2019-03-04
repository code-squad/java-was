package webserver;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpResponse {
    private static final Logger logger = getLogger(HttpResponse.class);
    StringBuilder sb = new StringBuilder();
    DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void addHeader(String statusCode, String location) {}

    public void forward(String contentType) throws IOException {
        this.sb.append("Content-Type: ");

        if (contentType.endsWith("html")) {
            sb.append("text/html;");
        } else {
            sb.append("text/css;");
        }
        sb.append("charset=utf-8\r\n");

    }

    public void forwardBody(String string) {
    }

    public void response200Header(int lengthOfBodyContent) throws IOException {
        sb.append("HTTP/1.1 200 OK \r\n");
        sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        sb.append("\r\n");

        dos.writeBytes(sb.toString());
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendRedirect(String path, boolean isLogin) throws IOException {
        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Location : " + path + "\r\n");
        sb.append("Set-Cookie: logined=" + isLogin + "; Path=/\r\n");
        sb.append("\r\n");

        dos.writeBytes(sb.toString());
    }
    public void processHeader() {}
}
