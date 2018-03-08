package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
        if(requestHeader.get(3).contains("Content-Length")) {
            requestBody = getRequestBody(br);
        }
    }

    public boolean getCookieValue(){
        String cookieLine = requestHeader.get(requestHeader.size()-2);
        String cookies = HttpRequestUtils.parseHeader(cookieLine).getValue();
        log.debug("cookies : {}", cookies.toString());
        Map<String, String> values = HttpRequestUtils.parseCookies(cookies);
        boolean loginStatus = Boolean.parseBoolean(values.get("logined"));
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

    public String getQueryString(String URI){
        return URI.split("\\?")[1];
    }

    public int getContentLength(){
        String line = requestHeader.get(3);
        String[] tokens = line.split(" ");
        return Integer.parseInt(tokens[1]);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestHeader=" + requestHeader +
                '}';
    }
}
