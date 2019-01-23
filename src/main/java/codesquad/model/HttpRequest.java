package codesquad.model;

import codesquad.model.responses.HttpResponse;
import codesquad.model.responses.ResponseCode;
import codesquad.util.HttpRequestUtils;
import codesquad.util.IOUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpRequest {
    private static final Logger log = getLogger(HttpRequest.class);

    private Url url;

    private int contentLength = 0;

    private Map<String, String> cookie = Maps.newHashMap();

    private List<String> accept = new ArrayList<>();

    private ResponseCode responseCode = ResponseCode.OK;

    public HttpRequest(Url url, Map<String, String> headers) {
        this.url = url;
        if (headers.containsKey("Accept")) this.accept = Arrays.asList(headers.get("Accept").split(","));
        if (headers.containsKey("Content-Length")) contentLength = Integer.parseInt(headers.get("Content-Length"));
        if (headers.containsKey("Cookie")) cookie = HttpRequestUtils.parseCookies(headers.get("Cookie"));
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

    public void setBodyValue(BufferedReader br) throws IOException {
        this.url.setQueryValue(IOUtils.readData(br, contentLength));
    }

    public boolean hasAllThoseFields(List<String> fields) {
        return url.hasAllThoseFields(fields);
    }

    public Object bindingQuery(Object aInstance) {
        return url.bindingQuery(aInstance);
    }

    public String getSID() {
        return cookie.get(HttpSession.COOKIE_KEY);
    }

    public HttpResponse toResponse() {
        return new HttpResponse(accept, responseCode, url.getAccessPath(), cookie);
    }

    @Override
    public String toString() {
        return "HttpRequest[url=" + url + ", contentLength=" + contentLength + ", cookie=" + cookie +
                ", accept=" + accept + ", responseCode=" + responseCode + ']';
    }
}
