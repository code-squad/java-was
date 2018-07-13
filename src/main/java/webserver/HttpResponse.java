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
    private static final int FOUND = 302;
    private static final int OK = 200;
    private DataOutputStream response;

    public HttpResponse(OutputStream response) {
        this.response = new DataOutputStream(response);
    }

    public void getResponse(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        responseHeader(body.length);
        responseBody(body);
    }

    public void loginFailed(String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        responseHeaderWithCookie(body.length);
        responseBody(body);
    }

    public void getStylesheet(String url) throws IOException{
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        getStylesheet(body.length);
        responseBody(body);
    }

    public void getStylesheet(int lengthOfBodyContent) {
        try {
            response.writeBytes(responseStatus(200));
            response.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            response.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeaderWithCookie(int lengthOfBodyContent) {
        try {
            response.writeBytes(responseStatus(200));
            response.writeBytes("Set-Cookie: logined=false; Path=/\r\n");
            response.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            response.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeader(int lengthOfBodyContent) {
        try {
            response.writeBytes(responseStatus(200));
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

    public void redirect(String redirectUrl) {
        try {
            response.writeBytes(responseStatus(FOUND));
            response.writeBytes("Location: " + DOMAIN + redirectUrl + "\r\n");
            response.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void redirectWithCookie(String redirectUrl) throws IOException {
        response.writeBytes(responseStatus(FOUND));
        response.writeBytes("Location: " + DOMAIN + redirectUrl + "\r\n");
        response.writeBytes("Set-Cookie: logined=true; Path=/\r\n");
        response.writeBytes("\r\n");
    }

    public static String responseStatus(int statusCode) {
        if (statusCode == FOUND) {
            return "HTTP/1.1 302 Found \r\n";
        }
        return "HTTP/1.1 200 OK \r\n";
    }
}
