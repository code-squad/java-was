package codesquad.model;

import com.google.common.collect.Maps;
import codesquad.util.HttpRequestUtils;

import java.util.Map;
import java.util.Objects;

public class Url {
    public static final String ROOT_STATIC_PATH = "./webapp";
    public static final String QUESTION_MARK = "\\?";
    public static final String BLANK = " ";

    private RequestMethod requestMethod;

    private String accessPath;

    private Map<String, String> queryString;

    public Url(RequestMethod requestMethod, String accessPath) {
        this.requestMethod = requestMethod;
        this.accessPath = accessPath;
    }

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

    public RequestMethod getRequestMethod() {
        return requestMethod;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return requestMethod == url.requestMethod &&
                accessPath.equals(url.accessPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestMethod, accessPath);
    }
}
