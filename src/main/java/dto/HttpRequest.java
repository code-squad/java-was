package dto;

import exception.HttpParseException;
import util.HttpRequestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class HttpRequest {
    private static final String COOKIE = "Cookie";
    private String[] startLine;
    private Map<String, String> headers;
    private Map<String, String> queries;

    public HttpRequest(String[] startLines, Map<String, String> headers, Map<String,String> queries) {
        this.startLine = startLines;
        this.headers = headers;
        this.queries = queries;
    }

    public String method() {
        return this.startLine[0];
    }

    public String url() {
        return this.startLine[1];
    }

    public String version(){
        if(startLine.length == 3) {
            return this.startLine[2];
        }
        return "HTTP/0.9";
    }

    public String header(String key) {
        if (this.headers.containsKey(key)) {
            return this.headers.get(key);
        }
        throw new HttpParseException("존재하지 않는 헤더의 키");
    }

    public String query(String key) {
        if (this.queries.containsKey(key)) {
            try {
                return URLDecoder.decode(this.queries.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public boolean isTrueCookie(String key) {
        if (headers.containsKey(COOKIE)) {
            String cookie = headers.get(COOKIE);
            return HttpRequestUtils.parseCookies(cookie).get(key).equals("true");
        }
        return false;
    }
}
