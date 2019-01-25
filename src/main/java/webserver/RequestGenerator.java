package webserver;

import webserver.http.request.HttpRequest;
import webserver.http.request.RequestBody;
import webserver.http.request.RequestHeaders;
import webserver.http.request.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpBodyUtils;
import util.HttpHeaderUtils;
import util.HttpLineUtils;


import java.io.BufferedReader;
import java.io.IOException;


public class RequestGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RequestGenerator.class);

    public static HttpRequest generateRequest(BufferedReader br) throws IOException {
        RequestLine requestLine = HttpLineUtils.parseRequestLine(br);
        RequestHeaders requestHeaders = HttpHeaderUtils.parseRequestHeaders(br);
        RequestBody requestBody = HttpBodyUtils.parseRequestBody(br, requestHeaders.getContentLength());

        return new HttpRequest(requestLine, requestHeaders, requestBody);
    }
}
