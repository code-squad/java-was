package model;

import constants.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private HttpMethod httpMethod;
    private String path;

    public HttpRequest(BufferedReader br) {
        try {
            String requestHeader = br.readLine();
            if (requestHeader == null) {
                return;
            }
            String[] tokens = requestHeader.split(" ");
            this.httpMethod = HttpMethod.valueOf(tokens[0].trim());
            this.path = tokens[1].trim();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }
}
