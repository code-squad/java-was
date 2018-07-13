package webserver;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpParameter {

    private Map<String, String> queryString;

    public HttpParameter(String params){
        this.queryString = HttpRequestUtils.parseQueryString(params);
    }

    public String findParameter(String key){
        return queryString.get(key);
    }
}
