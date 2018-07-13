package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private HttpUrl url;
    private HttpMethod method;
    private HttpHeader httpHeader;
    private HttpParameter parameters;

    public HttpRequest(String url, HttpMethod method, HttpHeader header, BufferedReader br) throws IOException {
        this.method = method;
        this.httpHeader = header;

        List<String> urlAndQueryString = Arrays.asList(url.split("\\?"));
        this.url = new HttpUrl(urlAndQueryString.get(0).trim());
        this.parameters = new  HttpParameter(existParameter(urlAndQueryString, br));
    }

    public String getUrl() {
        return url.getUrl();
    }

    public String getMethod() {
        return method.getMethod();
    }

    public String getHeader(String key) {
        return httpHeader.findHeader(key);
    }

    public String getParams(String key){
        return parameters.findParameter(key);
    }

    public String existParameter(List<String> url, BufferedReader br) throws IOException {
        if (!method.isGetMethod()) {
            return IOUtils.readData(br, httpHeader.getContentLength());
        }
        return hasUrl(url);
    }

    public String hasUrl(List<String> url) {
        if (url.size() != 2) {
            return null;
        }
        return url.get(1).trim();
    }
}
