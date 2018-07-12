package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestLine {
    private static final Logger logger = LoggerFactory.getLogger(RequestLine.class);
    private final HttpMethod method;
    private final String url;


    public RequestLine(BufferedReader reader) throws IOException {
        try {
            String[] requestLine = HttpRequestUtils.parseRequestLine(reader.readLine());
            this.method = HttpMethod.ofValue(requestLine[0]);
            this.url = HttpRequestUtils.decodeUrlEncoding(requestLine[1]);

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

    String getPath() {
        return parseUrl()[0];
    }

    public boolean isMethod(HttpMethod method) {
        return this.method == method;
    }

    public Map<String, String> getQueryParameters() {
        return HttpRequestUtils.parseQueryString(getQueryString());
    }

    @Override
    public String toString() {
        return method + " " + url;
    }
}