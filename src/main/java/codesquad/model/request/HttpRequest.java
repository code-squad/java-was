package codesquad.model.request;

import codesquad.model.HttpSession;
import codesquad.model.responses.HttpResponse;
import codesquad.model.responses.ResponseCode;
import codesquad.util.HttpRequestUtils;
import codesquad.util.IOUtils;
import codesquad.util.ReflectionUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpRequest {
    private static final Logger log = getLogger(HttpRequest.class);

    private Url url;

    private Map<String, String> queryValue;

    private int contentLength;

    private Map<String, String> cookie;

    private List<String> accept;

    private ResponseCode responseCode = ResponseCode.OK;

    public HttpRequest(InputStream in) throws IOException {
        Map<HttpRequestKey, String> headers = IOUtils.readHeader(in);
        url = new Url(HttpMethod.of(headers.get(HttpRequestKey.HTTP_METHOD)), headers.get(HttpRequestKey.ACCESS_PATH));
        accept = Arrays.asList(headers.getOrDefault(HttpRequestKey.ACCEPT, "").split(","));
        contentLength = Integer.parseInt(headers.getOrDefault(HttpRequestKey.CONTENT_LENGTH, "0"));
        cookie = HttpRequestUtils.parseCookies(headers.getOrDefault(HttpRequestKey.COOKIE, ""));
        queryValue = HttpRequestUtils.parseQueryString(headers.get(HttpRequestKey.QUERY_VALUE));
    }

    public void addCookie(HttpSession httpSession) {
        httpSession.putCookie(cookie);
    }

    public void generateResponseCode(Object result) {
        String newAccessPath = (String) result;
        if (!newAccessPath.startsWith("redirect:")) {
            url.renewAccessPath(newAccessPath);
            return;
        }
        url.renewAccessPath(newAccessPath.split(":")[1]);
        responseCode = ResponseCode.FOUND;
    }

    public Method findMappingMethod(Map<Url, Method> mappingHandler) {
        return mappingHandler.get(this.url);
    }

    public boolean hasAllThoseFields(List<String> fields) {
        if (queryValue.isEmpty()) return false;
        for (String key : queryValue.keySet()) {
            if (!fields.contains(key)) return false;
        }
        return true;
    }

    public Object bindingQueryValue(Object aInstance) {
        Arrays.stream(aInstance.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set"))
                .filter(method -> queryValue.containsKey(ReflectionUtils.getFieldName(method.getName())))
                .forEach(method -> ReflectionUtils.injectValue(aInstance, method, queryValue));
        return aInstance;
    }

    public String getSID() {
        return cookie.get(HttpSession.COOKIE_KEY);
    }

    public HttpResponse toResponse() {
        return new HttpResponse(accept, responseCode, url.getAccessPath(), cookie);
    }

    public boolean hasMappingUrl(Map<Url, Method> mappingHandler) {
        return mappingHandler.containsKey(url);
    }

    @Override
    public String toString() {
        return "HttpRequest[url=" + url + ", queryValue=" + queryValue + ", contentLength=" + contentLength +
                ", cookie=" + cookie + ", accept=" + accept + ", responseCode=" + responseCode + ']';
    }
}
