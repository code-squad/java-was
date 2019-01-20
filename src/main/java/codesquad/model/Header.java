package codesquad.model;

import codesquad.util.HttpRequestUtils;
import codesquad.util.IOUtils;
import codesquad.util.responses.Response;
import codesquad.util.responses.ResponseCode;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Header {
    private static final Logger log = getLogger(Header.class);

    private Url url;

    private int contentLength = 0;

    private Map<String, String> cookie = Maps.newHashMap();

    private boolean cookieModified = false;

    private ResponseCode responseCode = ResponseCode.OK;

    public Header(Url url, Map<String, String> headers) {
        this.url = url;
        if (headers.containsKey("Content-Length")) contentLength = Integer.parseInt(headers.get("Content-Length"));
        if (headers.containsKey("Cookie")) cookie = HttpRequestUtils.parseCookies(headers.get("Cookie"));
    }

    public void addCookie(HttpSession httpSession) {
        cookieModified = true;
        httpSession.putCookie(cookie);
    }

    public Response getResponse(Map<ResponseCode, Response> responses) {
        return responses.get(responseCode);
    }

    public void generateResponseCode(Object result) {
        String newAccessPath = (String) result;
        if (newAccessPath.contains("redirect")) {
            url.renewAccessPath(newAccessPath.split(":")[1]);
            responseCode = ResponseCode.FOUND;
            return;
        }
        url.renewAccessPath(newAccessPath);
    }

    public String writeCookie() {
        if (cookie.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("Set-Cookie: ");
        for (String key : cookie.keySet()) {
            sb.append(key + "=" + cookie.get(key) + ";");
        }
        sb.append(" Path=/");
        return sb.toString();
    }

    public boolean isCookieModified() {
        return cookieModified;
    }

    public byte[] writeBody() throws IOException {
        return Files.readAllBytes(new File(url.generateFilePath()).toPath());
    }

    public Method findMappingMethod(Map<Url, Method> mappingHandler) {
        return mappingHandler.get(this.url);
    }

    public String generateAccessPath() {
        return this.url.generateAccessPath();
    }

    public void setBodyValue(BufferedReader br) throws IOException {
        this.url.setQueryValue(IOUtils.readData(br, contentLength));
    }

    public boolean hasSameFieldName(String name) {
        return url.hasSameFieldName(name);
    }

    public void injectValue(Object aInstance, Method method) {
        url.injectValue(aInstance, method);
    }

    public void putCookie(Map<String, Object> newCookie) {
        for (String key : cookie.keySet()) {
            newCookie.put(key, cookie.get(key));
        }
    }

    @Override
    public String toString() {
        return "Header{" +
                "url=" + url +
                ", contentLength=" + contentLength +
                ", cookie=" + cookie +
                ", cookieModified=" + cookieModified +
                ", responseCode=" + responseCode +
                '}';
    }
}
