package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    public static final String DOMAIN = "http://localhost:8070";
    private static final String COLON = ": ";
    private DataOutputStream response;

    public HttpResponse(OutputStream response) {
        this.response = new DataOutputStream(response);
    }

    public void addHeader(String key, String value) {
        try {
            response.writeBytes(key + COLON + value);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    public void forward(String url, ContentType contentType) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        responseHeader(body.length, contentType);
        responseBody(body);
    }

    public void modelAndViewResponse(ModelAndView modelAndView) throws Exception {
        byte[] body = modelAndView.createView();
        responseHeader(body.length, ContentType.HTML);
        responseBody(body);
    }

    private void responseHeader(int lengthOfBodyContent, ContentType contentType) {
        try {
            response.writeBytes("HTTP/1.1 " + StatusCode.OK + "\r\n");
            response.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            response.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void loginFailed(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        responseHeaderWithCookie(body.length);
        responseBody(body);
    }

    private void responseHeaderWithCookie(int lengthOfBodyContent) {
        try {
            response.writeBytes("HTTP/1.1 " + StatusCode.OK + "\r\n");
            response.writeBytes("Set-Cookie: logined=false; Path=/\r\n");
            response.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            response.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            response.write(body, 0, body.length);
            response.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String redirectUrl) {
        try {
            response.writeBytes("HTTP/1.1 " + StatusCode.FOUND + "\r\n");
            response.writeBytes("Location: " + DOMAIN + redirectUrl + "\r\n");
            response.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void redirectWithCookie(String redirectUrl) throws IOException {
        response.writeBytes("HTTP/1.1 " + StatusCode.FOUND + "\r\n");
        response.writeBytes("Location: " + DOMAIN + redirectUrl + "\r\n");
        response.writeBytes("Set-Cookie: logined=true; Path=/\r\n");
        response.writeBytes("\r\n");
    }
}
