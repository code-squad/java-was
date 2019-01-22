package codesquad.model;

import codesquad.model.responses.Response;
import codesquad.model.responses.ResponseCode;
import codesquad.util.HttpRequestUtils;
import codesquad.util.IOUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Header {
    private static final Logger log = getLogger(Header.class);

    private Url url;

    private int contentLength = 0;

    private Map<String, String> cookie = Maps.newHashMap();

    private boolean cookieModified = false;

    private List<String> accept;

    private ResponseCode responseCode = ResponseCode.OK;

    public Header(Url url, Map<String, String> headers) {
        this.url = url;
        if (headers.containsKey("Accept")) this.accept = Arrays.asList(headers.get("Accept").split(","));
        if (headers.containsKey("Content-Length")) contentLength = Integer.parseInt(headers.get("Content-Length"));
        if (headers.containsKey("Cookie")) cookie = HttpRequestUtils.parseCookies(headers.get("Cookie"));
    }

    public void addCookie(HttpSession httpSession) {
        cookieModified = true;
        httpSession.putCookie(cookie);
    }

    public void generateResponseCode(Object result) {
        String newAccessPath = (String) result;
        if (!newAccessPath.contains("redirect")) {
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

    public void putCookie(Map<String, Object> newCookie) {
        for (String key : cookie.keySet()) {
            newCookie.put(key, cookie.get(key));
        }
    }

    public Response toResponse() {
        return new Response(accept, responseCode, url.getAccessPath(), cookie);
    }

    @Override
    public String toString() {
        return "Header[url=" + url + ", contentLength=" + contentLength + ", cookie=" + cookie +
                ", cookieModified=" + cookieModified + ", accept=" + accept + ", responseCode=" + responseCode + ']';
    }
}
