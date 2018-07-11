package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.Pair;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final String CONTENT_LENGTH = "Content-Length";
    private final RequestLine requestLine;
    private final RequestHeaders headers;
    private final RequestBody body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        this.requestLine = new RequestLine(reader.readLine());

        Map<String, String> params = new HashMap<>();
        while (true) {
            String line = reader.readLine();
            if ("".equals(line) || line == null) {
                break;
            }
            Pair pair = HttpRequestUtils.parseHeader(line);
            params.put(pair.getKey(), pair.getValue());
        }
        this.headers = new RequestHeaders(params);

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

    public String getHeader(String key) {
        return headers.getHeader(key);
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
