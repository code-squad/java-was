package header;

public class HttpRedirectHeader implements HttpHeader {

    private static StringBuilder sb = new StringBuilder();

    private String target;

    public HttpRedirectHeader(String target) {
        this.target = target;
    }

    @Override
    public String generateHttpHeaderString() {
        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Location: " + this.target + "\r\n");
        return sb.toString();
    }

}
