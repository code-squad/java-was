package model;

import com.google.common.collect.Maps;
import util.HttpRequestUtils;

import java.util.Map;

public class Url {
    public static final String ROOT_STATIC_PATH = "./webapp";
    public static final String QUESTION_MARK = "\\?";
    public static final String BLANK = " ";

    private RequestMethod requestMethod;

    private String accessPath;

    private Map<String, String> queryString;

    private Url(RequestMethod requestMethod, String accessPath, Map<String, String> queryString) {
        this.requestMethod = requestMethod;
        this.accessPath = accessPath;
        this.queryString = queryString;
    }

    public static Url of(String url) {
        String[] parsedUrl = url.split(BLANK);
        String[] parsedPath = parsedUrl[1].split(QUESTION_MARK);
        RequestMethod requestMethod = RequestMethod.of(parsedUrl[0]);
        if(parsedPath.length == 1) return new Url(requestMethod, parsedPath[0], Maps.newHashMap());
        return new Url(requestMethod, parsedPath[0], HttpRequestUtils.parseQueryString(parsedUrl[1]));
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String generate() {
        return ROOT_STATIC_PATH + this.accessPath;
    }

    @Override
    public String toString() {
        return "Url[requestMethod=" + requestMethod + ", accessPath=" + accessPath + ", queryString=" + queryString + ']';
    }
}
