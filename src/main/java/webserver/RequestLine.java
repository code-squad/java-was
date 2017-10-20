package webserver;

import java.util.Map;

import util.HttpRequestUtils;
import util.StringUtils;
import util.HttpRequestUtils.RequestTypes;

public class RequestLine {
    
    String path;
    RequestTypes method;
    Map<String, String> params;
    
    public RequestLine(String line) {
        this.path = StringUtils.directoryFromRequestHeader(line);
        this.method = HttpRequestUtils.parseRequestType(line);
        this.params = HttpRequestUtils.parseQueryString(line);
    }

    public String getPath() {
        return path;
    }

    public RequestTypes getMethod() {
        return method;
    }

    public Map<String, String> getParams() {
        return params;
    }
    
    

}
