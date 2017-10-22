package header;

public class HttpOkHeader implements HttpHeader{
    
    
    private int lengthOfBody;
    private boolean resource;
    
    public HttpOkHeader(int lengthOfBody, boolean resource) {
        this.lengthOfBody = lengthOfBody;
        this.resource = resource;
    }

    @Override
    public String generateHttpHeaderString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK \r\n");
        if(this.isResource()) {
            sb.append("Content-Type: text/css;charset=UTF-8\r\n");
            sb.append("Content-Length: " + this.lengthOfBody());
            sb.append("\r\n");
        }
        else {
            sb.append("Content-Type: text/html;charset=UTF-8");
            sb.append("\r\n");
            sb.append("Content-Length: " + this.lengthOfBody());
            sb.append("\r\n");
        }
        
        return sb.toString();
    }
    
    private boolean isResource() {
        return this.resource;
    }
    
    public void setResource(boolean resource) {
        this.resource = resource;
    }
    
    public void setLengthOfBody(int length) {
        this.lengthOfBody = length;
    }
    
    private int lengthOfBody() {
        return this.lengthOfBody;
    }

}
