package webserver.http.request;

import vo.HttpMethod;

public class HttpRequest {
    private RequestLine requestLine;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;

    public HttpRequest() {
    }

    public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public HttpMethod getMethod() {
        return this.requestLine.getMethod();
    }

    public String getPath() {
        return this.requestLine.getUri();
    }

    public String getHeader(String key) {
        return this.requestHeaders.getHeader(key);
    }

    public String getParameter(String key) {
        return this.requestBody.getParameter(key);
    }
}
