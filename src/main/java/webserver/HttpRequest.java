package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.*;

import controller.RequestMethod;
import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestTypes;
import util.StringUtils;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine line;
    
    private String requestBody;

    private Map<String, String> headers;
    private Map<String, String> parameters;

    public HttpRequest(InputStream httpInputStream) throws UnsupportedEncodingException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(httpInputStream, "UTF-8"));
        try {
            this.line = new RequestLine(br.readLine());
            
            StringBuilder sb = new StringBuilder();
            StringBuilder bodyBuilder = new StringBuilder();
            while (br.readLine().equals("\r\n")) {
                sb.append(br.readLine());
            }
            this.headers = HttpRequestUtils.parseHttpRequestHeader(sb.toString());
            
            while (br.readLine() == null) {
                bodyBuilder.append(br.readLine());
            }
            this.requestBody = bodyBuilder.toString();
            
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public RequestLine getLine() {
        return this.line;
    }
    
    public String getRequestBody() {
        return this.requestBody;
    }

}
