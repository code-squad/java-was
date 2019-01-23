package http;

import java.util.List;

public class RequestHeaders {
    private List<Pair> headers;

    public RequestHeaders() {
    }

    public RequestHeaders(List<Pair> headers) {
        this.headers = headers;
    }

    public String getHeader(String key) {
        for (Pair header : headers) {
            if (header.getKey().equals(key))
                return header.getValue();
        }
        return null;
    }
}
