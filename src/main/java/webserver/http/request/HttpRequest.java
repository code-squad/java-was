package webserver.http.request;

import vo.HttpMethod;

public class HttpRequest {
    private RequestLine requestLine;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;

    public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public boolean matchMethod(HttpMethod method) {
        return this.requestLine.getMethod().equals(method);
    }

    public String getPath() {
        return this.requestLine.getUri();
    }

    public String getHeader(String key) {
        return this.requestHeaders.getHeader(key);
    }

    public String getParameter(String key) {
        String parameter = this.requestBody.getParameter(key);
        if (parameter == null)
            return this.requestLine.getQueryParameter(key);
        return parameter;
    }
}
