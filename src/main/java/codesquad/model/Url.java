package codesquad.model;

import codesquad.util.HttpRequestUtils;
import codesquad.webserver.WebServer;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class Url {
    public static final String ROOT_STATIC_PATH = "./webapp";
    public static final String QUESTION_MARK = "\\?";
    public static final String BLANK = " ";

    private RequestMethod requestMethod;

    private String accessPath;

    private Map<String, String> queryValue;

    public Url(RequestMethod requestMethod, String accessPath) {
        this.requestMethod = requestMethod;
        this.accessPath = accessPath;
    }

    public Url(RequestMethod requestMethod, String accessPath, Map<String, String> queryString) {
        this.requestMethod = requestMethod;
        this.accessPath = accessPath;
        this.queryValue = queryString;
    }

    public static Url of(String url) {
        String[] parsedUrl = url.split(BLANK);
        String[] parsedPath = parsedUrl[1].split(QUESTION_MARK);
        RequestMethod requestMethod = RequestMethod.of(parsedUrl[0]);
        if (parsedPath.length == 1) return new Url(requestMethod, parsedPath[0], Maps.newHashMap());
        return new Url(requestMethod, parsedPath[0], HttpRequestUtils.parseQueryString(parsedUrl[1]));
    }

    public String getAccessPath() {
        return accessPath;
    }

    public String generateAccessPath() {
        StringBuilder sb = new StringBuilder("http://");
        return sb.append(WebServer.SERVER_IP)
                .append(WebServer.DEFAULT_PORT)
                .append(this.accessPath).toString();
    }

    public String generateFilePath() {
        return ROOT_STATIC_PATH + this.accessPath;
    }

    public void setQueryValue(String bodyText) {
        if (!Strings.isNullOrEmpty(bodyText)) this.queryValue = HttpRequestUtils.parseQueryString(bodyText);
    }

    public boolean hasSameFieldName(String fieldName) {
        return this.queryValue.containsKey(fieldName);
    }

    public void injectValue(Object aInstance, Method method) {
        String setterName = method.getName().substring(3);
        String fieldName = setterName.substring(0, 1).toLowerCase() + setterName.substring(1);
        try {
            method.invoke(aInstance, this.queryValue.get(fieldName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renewAccessPath(String newAccessPath) {
        this.accessPath = newAccessPath;
    }

    @Override
    public String toString() {
        return "Url[requestMethod=" + requestMethod + ", accessPath=" + accessPath + ", queryValue=" + queryValue + ']';
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
