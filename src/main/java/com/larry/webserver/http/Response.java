package com.larry.webserver.http;

import com.larry.webserver.Latty;
import com.larry.webserver.mvc.viewFlow.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Response {

    public static final String DOMAIN = Latty.HOST + ":" + Latty.DEFAULT_PORT;

    private final Logger log = LoggerFactory.getLogger(Response.class);

    private String httpVersion;
    private HttpStatus HTTP_STATUS = HttpStatus.OK;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public Response() {
        headers.put("Content-Type", "text/html;charset=utf-8\r\n");
    }

    public Response(Request request, byte[] body, String viewFileName) {
        if (viewFileName.contains("redirect")) {
            viewFileName = viewFileName.replace("redirect:/", "");
            HTTP_STATUS = HttpStatus.FOUND;
            headers.put("Location", DOMAIN+"/" + viewFileName);
            log.debug("redirect location : {}", viewFileName);
        }
        this.body = body;
        httpVersion = request.getHttpVersion();

        headers.put("Content-Length", String.valueOf(body.length));
    }

    public void send(DataOutputStream dos) {
        responseHeader(dos, headers);
        responseBody(dos, body);
    }

    private void responseHeader(DataOutputStream dos, Map<String, String> headers) {
        try {
            dos.writeBytes(String.format("%s %s\r\n", httpVersion, HTTP_STATUS.toString()));
            headers.forEach((String k, String v) -> {
                try {
                    log.info("write bytes : {}", k + ": " + v + "\r\n");
                    dos.writeBytes(k + ": " + v + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static String getDOMAIN() {
        return DOMAIN;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpStatus getHTTP_STATUS() {
        return HTTP_STATUS;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setStatue(HttpStatus statue) {
        this.HTTP_STATUS = statue;
        log.info("http status changed : {}", HTTP_STATUS);
    }

    public void setView(ModelAndView modelAndView) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.body = modelAndView.resolveBody();
        this.headers.put("Content-Length", String.valueOf(body.length));
        if (HTTP_STATUS.equals(HttpStatus.FOUND)) {
            headers.put("Location", DOMAIN+"/" + modelAndView.getViewName());
        }
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void loginSuccess() {
        headers.put("Set-Cookie", "logined=true; Path=/");
    }
}
