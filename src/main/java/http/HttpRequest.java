package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> header;
    private Map<String, String> parameter;

    public HttpRequest(BufferedReader br) {
        parseRequest(br);
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getParameter(String key) {
        return parameter.get(key);
    }

    private void parseRequest(BufferedReader br) {
        String requestLine;
        try {
            requestLine = br.readLine();
            httpMethod = HttpMethod.valueOf(HttpRequestUtils.getMethod(requestLine));
            path = HttpRequestUtils.getURL(requestLine);
            header = HttpRequestUtils.extractHeader(br);
            String body = IOUtils.readData(br, Integer.parseInt(getHeader("Content-Length")));
            parameter= HttpRequestUtils.parseQueryString(body);
        } catch (IOException e) {
            log.error("HttpRequest parse 과정 에러");
        }
    }
}
