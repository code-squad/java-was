package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    // requestHeader 를 map 으로 바꾼다.
    public Map<String, String> header = new HashMap<>();
    public String requestBody;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        // add request line
        String line = br.readLine();
        header.put("requestLine", line);
        log.debug(line);
        // add other header
        readHeader(br);
        // add request body
        if(getHeader("Content-Length") != null) {
            requestBody = getRequestBody(br);
            log.debug(requestBody);
        }
    }

    private void readHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        if(line == null || line.equals("")) return;
        log.debug(line);
        HttpRequestUtils.Pair pair =  HttpRequestUtils.parseHeader(line);
        header.put(pair.getKey(), pair.getValue());
        readHeader(br);
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public String getMethod(){
        return getRequestLine().split(" ")[0];
    }

    public String getPath(){
        String URI = getRequestLine().split(" ")[1];
        if(URI.contains("?")) return URI.split("\\?")[0];
        return URI;
    }

    public String getParameter(String key){
        String URI = getRequestLine().split(" ")[1];
        return getRequestParameter(URI.split("\\?")[1]).get(key);
    }

    public Map<String, String> getRequestParameter(String queryString) {
        // extract user data
        return HttpRequestUtils.parseQueryString(queryString);
    }

    public String getQueryString(String URI){
        return URI.split("\\?")[1];
    }

    public String getRequestLine(){
        return getHeader("requestLine");
    }

    public boolean getCookieValue(){
        String cookie = getHeader("Cookie");
        boolean loginStatus = Boolean.parseBoolean(HttpRequestUtils.parseCookies(cookie).get("logined"));
        log.debug("loginStatus : {}", loginStatus);
        return loginStatus;
    }

    public String getContentType() {
        return getHeader("Accept");
    }

    public int getContentLength(){
        return Integer.parseInt(getHeader("Content-Length"));
    }

    private String getRequestBody(BufferedReader br) throws IOException {
        return IOUtils.readData(br, getContentLength());
    }

    public String getRequestBody(){
        return requestBody;
    }
//
//    public void printRequestHeader() {
//        header.forEach((key, value) -> {
//            if(key.equals("requestLine")) log.debug(value + "\n");
//            else log.debug(key + ": " + value + "\n");
//        });
//        log.debug("requestHeader :{");
//        log.debug("}");
//    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "header=" + header +
                ", requestBody='" + requestBody + '\'' +
                '}';
    }
}
