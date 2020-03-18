package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String path;

    public HttpRequest(BufferedReader br) {
        try {
            String requestHeader = br.readLine();
            if (requestHeader == null) {
                return;
            }
            String[] tokens = requestHeader.split(" ");
            this.method = tokens[0].trim();
            this.path = tokens[1].trim();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
