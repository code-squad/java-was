package webserver;

import java.util.Map;
import java.util.Optional;

public class HttpHeader {

    private Map<String, String> headers;

    public HttpHeader(Map<String, String> headers){
        this.headers = headers;
    }

    public String findHeader(String key){
        return headers.get(key);
    }

    public int getContentLength(){
        return Integer.parseInt(headers.get("Content-Length"));
    }
}
