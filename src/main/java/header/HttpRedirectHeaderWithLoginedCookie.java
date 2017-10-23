package header;

public class HttpRedirectHeaderWithLoginedCookie implements HttpHeader {

    private String target;
    private boolean logined;

    public HttpRedirectHeaderWithLoginedCookie(String target, boolean logined) {
        this.target = target;
        this.logined = logined;
    }

    @Override
    public String generateHttpHeaderString() {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Location: " + this.target + "\r\n");
        if (this.logined) {
            sb.append("Set-Cookie: logined=true; Path=/");
        }
        else sb.append("Set-Cookie: logined=false; Path=/");
        return sb.toString();
    }

}
