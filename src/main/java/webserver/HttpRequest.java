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
    public String requestLine;

    public HttpRequest(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        requestHeader.add(line);
        requestLine = requestHeader.get(0);
        tokens = requestLine.split(" ");
        while(!"".equals(line)){
            if(line == null) break;
            line = br.readLine();
            requestHeader.add(line);
        }
        if(hasTargetHeader("Content-Length")) {
            requestBody = getRequestBody(br);
        }
    }

    public boolean getCookieValue(){
        String cookie = getHeaderValue("Cookie");
        boolean loginStatus = Boolean.parseBoolean(HttpRequestUtils.parseCookies(cookie).get("logined"));
        log.debug("loginStatus : {}", loginStatus);
        return loginStatus;
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

    public String getURI() {
        return tokens[1];
    }

    public String getHTTPVersion() {
        return tokens[2];
    }

    public String getContentType() {
        return getHeaderValue("Accept");
    }

    public int getContentLength(){
        return Integer.parseInt(getHeaderValue("Content-Length"));
    }

    private String getHeaderValue(String header){
        String headerValue = "";
        for(String line : requestHeader){
            if(line.contains(header)) {
                headerValue = HttpRequestUtils.parseHeader(line).getValue();
                break;
            }
        }
        return headerValue;
    }

    public boolean hasTargetHeader(String header){
        for(String line : requestHeader){
            if(line.contains(header)) return true;
        }
        return false;
    }

    private String getRequestBody(BufferedReader br) throws IOException {
        int contentLength = getContentLength();
        return IOUtils.readData(br, contentLength);
    }

    public Map<String, String> getRequestParameter(String queryString) {
        // extract user data
        return HttpRequestUtils.parseQueryString(queryString);
    }

    public String getQueryString(String URI){
        return URI.split("\\?")[1];
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestHeader=" + requestHeader +
                '}';
    }
}
