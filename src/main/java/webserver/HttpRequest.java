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
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private RequestLine requestLine;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        requestLine = new RequestLine(line);
        log.debug(line);

        readHeader(br);

        if(getHeader("Content-Length") != null) {
            params = getRequestBody(br);
        }
        log.debug("\n");
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

    public String getPath(){
        return requestLine.getPath();
    }

    public HttpMethod getMethod(){
        return requestLine.getMethod();
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

    private Map<String, String> getRequestBody(BufferedReader br) throws IOException {
        String requestBody = IOUtils.readData(br, getContentLength());
        log.debug("requestBody : {}", requestBody);
        return HttpRequestUtils.parseQueryString(requestBody);
    }

    public String getParameter(String key){
        return params.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "header=" + header +
                ", params=" + params +
                ", requestLine=" + requestLine +
                '}';
    }
}
