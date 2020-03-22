package model;

import constants.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static constants.CommonConstants.ZERO_STRING;
import static constants.HttpMethod.POST;
import static constants.RequestHeaderConstants.CONTENT_LENGTH;
import static constants.RequestHeaderConstants.COOKIE;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private HttpMethod httpMethod;
    private String url;
    private String body;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();

    public HttpRequest(BufferedReader br) {
        try {
            if (readRequestLine(br)) {
                return;
            }

            readRequestHeader(br);
            parseCookie();

            if (POST.equals(httpMethod)) {
                readBody(br);
                parseBody();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void parseCookie() {
        this.cookies = HttpRequestUtils.parseCookies(headers.getOrDefault(COOKIE, ""));
    }

    private void readRequestHeader(BufferedReader br) throws IOException {
        while (true) {
            String line = br.readLine();

            if ("".equals(line)) {
                break;
            }
            String[] header = line.split(": ");
            headers.put(header[0], header[1]);
        }
    }

    private boolean readRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null) {
            return true;
        }
        parseRequestLine(requestLine);
        return false;
    }

    private void parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        this.httpMethod = HttpMethod.valueOf(tokens[0].trim());
        String url = tokens[1].trim();
        switch (httpMethod) {
            case POST:
                this.url = url;
                return;
            case GET:
                int index = url.indexOf("?");
                if (index == -1) {
                    this.url = url;
                } else {
                    this.url = url.substring(0, index);
                    String queryString = url.substring(index + 1);
                    this.params = HttpRequestUtils.parseQueryString(queryString);
                }
        }

    }

    public boolean isUrlNull() {
        return this.url == null;
    }

    private void readBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH, ZERO_STRING));
        this.body = IOUtils.readData(br, contentLength);
    }

    private void parseBody() {
        this.params = HttpRequestUtils.parseQueryString(this.body);
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
