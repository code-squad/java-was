package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.*;

import com.google.common.collect.Maps;

import controller.RequestMethod;
import requestmapping.RequestLine;
import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestTypes;
import util.IOUtils;
import util.StringUtils;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine line;

    private String requestBody;

    private Map<String, String> headers = Maps.newHashMap();
    private Map<String, String> parameters;

    public HttpRequest(InputStream httpInputStream) throws UnsupportedEncodingException {

        BufferedReader br = new BufferedReader(new InputStreamReader(httpInputStream, "UTF-8"));
        try {
            this.line = new RequestLine(br.readLine());
            log.debug(this.line.toString());
            String header = br.readLine();
            while (!header.equals("")) {
                log.debug("header : {}", header);
                String[] splitedHeader = header.split(": ");
                headers.put(splitedHeader[0], splitedHeader[1]);
                header = br.readLine();
            }
            log.debug("hello. this is a body part. ");
            this.requestBody = IOUtils.readData(br, Integer.parseInt(this.headers.get("Content-Length")));

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
