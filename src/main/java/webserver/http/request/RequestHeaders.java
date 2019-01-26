package webserver.http.request;

import util.HttpRequestUtils;
import webserver.http.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeaders {
    private static String EMPTY_HEADER = "";
    private List<Pair> headers;

    public RequestHeaders() {
    }

    public RequestHeaders(List<Pair> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        for (Pair header : headers)
            if (header.getKey().equals(key))
                return header.getValue();

        return EMPTY_HEADER;
    }

    public int getContentLength() {
        String contentLength = getHeader("Content-Length");
        if (contentLength.equals(EMPTY_HEADER)) {
            return 0;
        }
        return Integer.parseInt(contentLength);
    }

    public Map<String, String> getCookie() {
        String cookie = getHeader("Cookie");
        if (cookie.equals(EMPTY_HEADER))
            return new HashMap<>();
        return HttpRequestUtils.parseCookies(cookie);
    }
}
