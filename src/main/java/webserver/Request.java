package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PathParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class Request {

    private final Logger log = LoggerFactory.getLogger(Request.class);
    private final String httpVersion;
    HttpMethod httpMethod;
    String path;
    Map<String, String> params;

    public Request(BufferedReader br) throws IOException {
        PathParams pathParams = new PathParams(br);
        httpMethod = pathParams.parseHttpMethod();
        path = pathParams.parsePath();
        params = pathParams.parseParams();
        httpVersion = pathParams.getHttpVersion();
        log.debug("request http method : {}", httpMethod);
        log.debug("request http path : {}", path);
        log.debug("request http params : {}", params);
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
}
