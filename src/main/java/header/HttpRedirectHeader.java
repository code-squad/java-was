package header;

public class HttpRedirectHeader implements HttpHeader {
    
    private static final StringBuilder sb = new StringBuilder();
    
    private String target;
    
    
    public HttpRedirectHeader(String target) {
        this.target = target;
    }
    
    @Override
    public String generateHttpHeaderString() {
        
        
        return sb.toString();
    }

}
