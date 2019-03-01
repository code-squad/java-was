package webserver;

import model.HttpMethod;
import util.HttpRequestUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpRequest {
    private List<HttpRequestUtils.Pair> httpHeader = new ArrayList<>();
    private HttpMethod httpMethod;
    private String path;

    public HttpRequest(String requestLine) {
        httpMethod = HttpRequestUtils.getHttpMethod(requestLine);
        path = HttpRequestUtils.getPath(requestLine);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void getHeader(String header) {

        httpHeader.add(HttpRequestUtils.parseHeader(header));

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpHeader=" + httpHeader +
                ", httpMethod=" + httpMethod +
                '}';
    }
}
