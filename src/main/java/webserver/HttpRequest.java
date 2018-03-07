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

    public List<String> requestMessage = new ArrayList<>();
    public String[] tokens;

    public HttpRequest(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        requestMessage.add(line);
        while(!"".equals(line)){
            line = br.readLine();
            requestMessage.add(line);
        }
        // body
        String requestLine = requestMessage.get(0);
        tokens = requestLine.split(" ");
    }

    public List<String> getRequestMessage() {
        return requestMessage;
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
        String requestBody = IOUtils.readData(br, contentLength);
        requestMessage.add(requestBody);
        return requestBody;
    }

    public Map<String, String> getRequestParameter(String URI) {
        // extract user data
        return HttpRequestUtils.parseQueryString(getQueryString(URI));
    }

    private Map<String, String> post() {
        // post
        String requestBody = requestMessage.get(requestMessage.size()-1);
        log.debug("requestBody : {}", requestBody);
        return HttpRequestUtils.parseQueryString(requestBody);
    }

    public String getQueryString(String requestLine){
        return requestLine.split("\\?")[1];
    }

    public int getContentLength(){
        String line = requestMessage.get(3);
        String[] tokens = line.split(" ");
        return Integer.parseInt(tokens[1]);
    }

}
