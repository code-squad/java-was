package com.larry.webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.larry.webserver.util.PathParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class Request {

    private final Logger log = LoggerFactory.getLogger(Request.class);
    private final String httpVersion;
    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> params;
    private Map<String, String> headers;

    public Request(BufferedReader br) throws IOException {
        PathParams pathParams = new PathParams(br);
        httpMethod = pathParams.parseHttpMethod();
        path = pathParams.parsePath();
        params = pathParams.parseParams();
        httpVersion = pathParams.getHttpVersion();
        headers = pathParams.getHeaders();
        log.debug("request http method : {}", httpMethod);
        log.debug("request http path : {}", path);
        log.debug("request http params : {}", params);
        log.debug("request http headers : {}", headers);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public boolean getCookie() {
        log.info("cookie boolean {}", headers.get("Cookie"));
        return Boolean.parseBoolean(headers.get("Cookie").split(";")[0].split("=")[1]);
    }
}
