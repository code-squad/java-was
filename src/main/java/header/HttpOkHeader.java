package header;

public class HttpOkHeader implements HttpHeader{
    
    private static final StringBuilder sb = new StringBuilder();
    private int lengthOfBody;
    private boolean resource;

    @Override
    public String generateHttpHeaderString() {
        sb.append("HTTP/1.1 200 OK \r\n");
        if(this.isResource()) {
            sb.append("Content-Type: text/css;charset=UTF-8");
            sb.append("Content-Length: " + this.lengthOfBody());
            sb.append("\r\n");
        }
        else {
            sb.append("Content-Type: text/html;charset=UTF-8");
            sb.append("Content-Length: " + this.lengthOfBody());
        }
        
        return sb.toString();
    }
    
    private boolean isResource() {
        return this.resource;
    }
    
    private int lengthOfBody() {
        return this.lengthOfBody;
    }

}
