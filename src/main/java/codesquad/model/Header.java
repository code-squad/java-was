package codesquad.model;

import codesquad.util.HttpRequestUtils;
import codesquad.util.IOUtils;
import codesquad.util.responses.ResponseCode;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class Header {

    private Url url;

    private int contentLength = 0;

    private Map<String, String> cookie = Maps.newHashMap();

    private boolean cookieModified = false;

    private ResponseCode responseCode = ResponseCode.OK;

    public Header() {
    }

    public Header(Url url, Map<String, String> headers) {
        this.url = url;
        if (headers.containsKey("Content-Length")) contentLength = Integer.parseInt(headers.get("Content-Length"));
        if (headers.containsKey("Cookie")) cookie = HttpRequestUtils.parseCookies(headers.get("Cookie"));
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public void addCookie(HttpSession httpSession) {
        cookieModified = true;
        httpSession.putCookie(cookie);
    }

    public ResponseCode getResponseCode() {
        return responseCode;
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

    public void setQueryValue(BufferedReader br) throws IOException {
        url.setQueryValue(IOUtils.readData(br, contentLength));
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

    @Override
    public String toString() {
        return "Header{" +
                "url=" + url +
                ", contentLength=" + contentLength +
                ", cookie=" + cookie +
                ", responseCode=" + responseCode +
                '}';
    }

}
