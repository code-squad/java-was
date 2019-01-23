package webserver;

import http.HttpRequest;
import http.RequestHeaders;
import http.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpHeaderUtils;
import util.HttpLineUtils;


import java.io.BufferedReader;
import java.io.IOException;


public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    public static HttpRequest generateRequest(BufferedReader br) throws IOException {
        RequestLine requestLine = HttpLineUtils.parseRequestLine(br.readLine());
        RequestHeaders requestHeaders = HttpHeaderUtils.parseRequestHeaders(br);


        return new HttpRequest();
    }
}
