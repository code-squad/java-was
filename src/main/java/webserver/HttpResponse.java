package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    public static final String DOMAIN = "http://localhost:8070";
    private static final String COLON = ": ";
    private Map<String, String> headers;
    private DataOutputStream response;

    public HttpResponse(OutputStream response) {
        this.response = new DataOutputStream(response);
        headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void forward(String url, ContentType contentType) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            headers.put("Content-Type", contentType + ";charset=utf-8");
            headers.put("Content-Length", body.length + "");
            response200Header();
            responseBody(body);
        }catch (IOException e){
            log.debug(e.getMessage());
        }
    }

    public void modelAndViewResponse(ModelAndView modelAndView) {
        try {
            byte[] body = modelAndView.createView();
            headers.put("Content-Type", ContentType.HTML + ";charset=utf-8");
            headers.put("Content-Length", body.length + "");
            response200Header();
            responseBody(body);
        }catch (Exception e){
            log.debug(e.getMessage());
        }
    }

    public void sendRedirect(String redirectUrl) {
        try {
            response.writeBytes("HTTP/1.1 " + StatusCode.FOUND + "\r\n");
            response.writeBytes("Location: " + redirectUrl + "\r\n");
            insertHeader();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response200Header() {
        try {
            response.writeBytes("HTTP/1.1 " + StatusCode.OK + "\r\n");
            insertHeader();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void insertHeader() throws IOException {
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            response.writeBytes(key + COLON + headers.get(key) + "\r\n");
        }
        response.writeBytes("\r\n");
    }

    private void responseBody(byte[] body) {
        try {
            response.write(body, 0, body.length);
            response.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
