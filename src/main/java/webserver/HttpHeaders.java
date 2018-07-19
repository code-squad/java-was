package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private final Map<HttpHeader, String> headers = new HashMap<>();

    public HttpHeaders() {
    }

    public void addHeader(HttpHeader header, String value) {
        headers.put(header, value);
    }

    public boolean containsHeader(HttpHeader header) {
        return headers.containsKey(header);
    }

    public String getHeader(HttpHeader header) {
        return headers.get(header);
    }

    public String buildHeaders() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<HttpHeader, String> entry : headers.entrySet()) {
            builder.append(entry.getKey().toString());
            builder.append(": ");
            builder.append(entry.getValue()).append("\r\n");
        }
        return builder.toString();
    }
}
