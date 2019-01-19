package codesquad.model;

import codesquad.util.responses.ResponseCode;

public class Request {

    private Url url;

    private int contentLength;

    private Cookie cookie = new Cookie();

    private ResponseCode responseCode = ResponseCode.OK;

    public Request() {
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void generateResponseCode(Object result) {
        String newAccessPath = (String) result;
        if(!newAccessPath.contains("redirect")) {
            url.renewAccessPath(newAccessPath);
            return;
        }
        url.renewAccessPath(newAccessPath.split(":")[1]);
        responseCode = ResponseCode.FOUND;
    }

    public boolean hasCookieVal() {
        return cookie.hasValue();
    }

    public String writeCookie() {
        return cookie.writeCookie() + " Path=/";
    }

    @Override
    public String toString() {
        return "Request{" +
                "url=" + url +
                ", contentLength=" + contentLength +
                ", cookie=" + cookie +
                ", responseCode=" + responseCode +
                '}';
    }
    @Override
    public Request clone() throws CloneNotSupportedException {
        return (Request) super.clone();
    }

}
