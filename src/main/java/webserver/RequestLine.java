package webserver;

import util.HttpRequestUtils;

public class RequestLine {
    private HttpMethod method;
    private String url;

    public RequestLine(String line) {
        String[] requestLine = HttpRequestUtils.parseRequestLine(line);
        this.method = HttpMethod.ofValue(requestLine[0]);
        this.url = requestLine[1];
    }

    String[] parseUrl() {
        return url.split("\\?");
    }

    public Resource getResource() {
        String uri = parseUrl()[0];
        return new Resource(uri);
    }

    public String getQueryString() {
        return parseUrl()[1];
    }
}
