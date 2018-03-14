package webserver;

import util.HttpRequestUtils;

import java.util.Map;

public class RequestLine {
    private String requestLine;

    public RequestLine(String requestLine){
        this.requestLine = requestLine;
    }

    public HttpMethod getMethod(){
        String method = requestLine.split(" ")[0];
        return HttpMethod.valueOf(method);
    }

    public String getURI(){
        return requestLine.split(" ")[1];
    }

    public String getPath(){
        if(getURI().contains("?")) return getURI().split("\\?")[0];
        return getURI();
    }

    public String getParameter(String key){
        Map<String, String> p = HttpRequestUtils.parseQueryString(getURI().split("\\?")[1]);
        return p.get(key);
    }
}
