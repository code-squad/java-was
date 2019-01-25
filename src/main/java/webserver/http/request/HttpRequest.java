package webserver.http.request;

import util.HttpRequestUtils;

import java.util.List;
import java.util.Map;

public class HttpRequest {
    private static String TRUE = "true";

    private RequestLine requestLine;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;

    public HttpRequest() {
    }

    public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
    }

    public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestBody requestBody) {
        this.requestLine = requestLine;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public String getUri() {
        return this.requestLine.getUri();
    }

    public boolean isLogined() {
        Map<String, String> cookie = this.requestHeaders.getCookie();
        if (cookie.isEmpty())
            return false;
        return cookie.get("logined").equals(TRUE);
    }

    public String getAcceptType() {
        List<String> accepts = this.requestHeaders.getAccepts();
        if(accepts.isEmpty())
            return "";
        return accepts.get(0);
    }
}
