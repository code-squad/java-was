package model;

import java.util.HashMap;
import java.util.Map;

public class HttpHeader {

    private Map<String, String> header;

    public HttpHeader() {
        header = new HashMap<>();
    }

    public HttpHeader(Map<String, String> header) {
        this.header = header;
    }

    public void addHeader(String key, String value) {
        this.header.put(key, value);
    }

    public String obtainHeader(String headerName) {
        return this.header.get(headerName);
    }

    public boolean isResource() {
        return header.get("Accept").contains("text/css");
    }

    public int obtainContentLength() {
        if(header.containsKey("Content-Length")) {
            return Integer.parseInt(header.get("Content-Length"));
        }

        return 0;
    }

}
