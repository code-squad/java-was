package codesquad.model;

import codesquad.util.HttpRequestUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class Url {
    private static final Logger log = getLogger(Url.class);

    private HttpMethod httpMethod;

    private String accessPath;

    private Map<String, String> queryValue;

    public Url(HttpMethod httpMethod, String accessPath) {
        this.httpMethod = httpMethod;
        this.accessPath = accessPath;
    }

    public Url(HttpMethod httpMethod, String accessPath, Map<String, String> queryString) {
        this.httpMethod = httpMethod;
        this.accessPath = accessPath;
        this.queryValue = queryString;
    }

    public static Url of(String url) {
        return HttpRequestUtils.parseUrl(url);
    }

    public String getAccessPath() {
        return this.accessPath;
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

    public Object bindingQuery(Object aInstance) {
        Arrays.stream(aInstance.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set"))
                .filter(method -> queryValue.containsKey(getFieldName(method.getName())))
                .forEach(method -> injectValue(aInstance, method));
        return aInstance;
    }

    private String getFieldName(String methodName) {
        String setterName = methodName.substring(3);
        return setterName.substring(0, 1).toLowerCase() + setterName.substring(1);
    }

    public boolean hasAllThoseFields(List<String> fields) {
        if (queryValue.isEmpty()) return false;
        for (String key : queryValue.keySet()) {
            if (!fields.contains(key)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Url[httpMethod=" + httpMethod + ", accessPath=" + accessPath + ", queryValue=" + queryValue + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return httpMethod == url.httpMethod &&
                accessPath.equals(url.accessPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, accessPath);
    }
}
