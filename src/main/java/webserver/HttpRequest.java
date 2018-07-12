package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final String CONTENT_LENGTH = "Content-Length"; //많아지면 enum
    private final RequestLine requestLine;
    private final RequestHeaders headers;
    private final RequestBody body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        this.requestLine = new RequestLine(reader);
        this.headers = new RequestHeaders(reader);
        if (requestLine.isMethod(HttpMethod.POST)) {
            String body = IOUtils.readData(reader, Integer.parseInt(headers.getHeader(CONTENT_LENGTH)));
            this.body = RequestBody.of(body);
            return;
        }
        this.body = RequestBody.ofEmpty();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public RequestBody getBody() {
        return body;
    }

    public Map<String, String> getParameters() {
        if (requestLine.isMethod(HttpMethod.GET)) {
            return requestLine.getQueryParameters();
        }
        return body.getParameters();
    }
}
