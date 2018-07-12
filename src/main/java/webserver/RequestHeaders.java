package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;


public class RequestHeaders {
    private final HttpHeaders headers = new HttpHeaders();

    public RequestHeaders(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if ("".equals(line) || line == null) {
                break;
            }
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.addHeader(HttpHeader.getByValue(pair.getKey()), pair.getValue());
        }
    }

    public String getHeader(HttpHeader header) {
        return headers.getHeader(header);
    }

    public boolean containsHeader(HttpHeader header) {
        return headers.containsHeader(header);
    }
}
