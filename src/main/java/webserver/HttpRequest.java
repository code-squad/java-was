package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public List<String> requestHeader = new ArrayList<>();
    public String[] tokens;
    public String requestBody;

    public HttpRequest(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        requestHeader.add(line);
        String requestLine = requestHeader.get(0);
        tokens = requestLine.split(" ");
        while(!"".equals(line)){
            line = br.readLine();
            requestHeader.add(line);
            if(line == null) break;
        }
        if(requestHeader.get(3) == "Content-Length") {
            requestBody = getRequestBody(br);
        }
    }

    public String getRequestBody() {
        return requestBody;
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public String getHTTPMethod(){
        return tokens[0];
    }

    public String getURI(){
        return tokens[1];
    }

    public String getHTTPVersion(){
        return tokens[2];
    }

    private String getRequestBody(BufferedReader br) throws IOException {
        int contentLength = getContentLength();
        return IOUtils.readData(br, contentLength);
    }

    public Map<String, String> getRequestParameter(String queryString) {
        // extract user data
        return HttpRequestUtils.parseQueryString(queryString);
    }

    private Map<String, String> post() {
        // post
        String requestBody = requestHeader.get(requestHeader.size()-1);
        log.debug("requestBody : {}", requestBody);
        return HttpRequestUtils.parseQueryString(requestBody);
    }

    public String getQueryString(String URI){
        return URI.split("\\?")[1];
    }

    public int getContentLength(){
        String line = requestHeader.get(3);
        String[] tokens = line.split(" ");
        return Integer.parseInt(tokens[1]);
    }

}
