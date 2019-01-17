package model;

import com.google.common.collect.Maps;
import util.HttpRequestUtils;

import java.util.Map;

public class Url {
    public static final String QUESTION_MARK = "\\?";
    public static final String ROOT_STATIC_PATH = "./webapp";

    private String accessPath;

    private Map<String, String> queryString;

    private Url(String accessPath, Map<String, String> queryString) {
        this.accessPath = accessPath;
        this.queryString = queryString;
    }

    public static Url of(String url) {
        String[] parsedUrl = url.split(QUESTION_MARK);
        if(parsedUrl.length == 1) return new Url(parsedUrl[0], Maps.newHashMap());
        return new Url(parsedUrl[0], HttpRequestUtils.parseQueryString(parsedUrl[1]));
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String generate() {
        return ROOT_STATIC_PATH + this.accessPath;
    }
}
