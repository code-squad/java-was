package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestLine {
    private static final Logger logger = LoggerFactory.getLogger(RequestLine.class);

    private HttpMethod method;
    private String url;

    public RequestLine(String line) {
        try {
            String[] requestLine = HttpRequestUtils.parseRequestLine(line);
            this.method = HttpMethod.ofValue(requestLine[0]);
            this.url = HttpRequestUtils.decode(requestLine[1]);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    String[] parseUrl() {
        return url.split("\\?");
    }

    String getQueryString() {
        return parseUrl()[1];
    }

    public String getPath() {
        return parseUrl()[0];
    }

    public Map<String, String> getQueryParameters() {
        return HttpRequestUtils.parseQueryString(getQueryString());
    }
}