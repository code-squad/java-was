package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static util.HttpRequestUtils.Pair;

public class HttpRequest {
    private final RequestLine requestLine;
    private final List<Pair> params;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = reader.readLine();
        this.requestLine = new RequestLine(line);
        this.params = new ArrayList<>();
        while (!"".equals(line)) {
            if (line == null) {
                return;
            }
            line = reader.readLine();
            Pair pair = HttpRequestUtils.parseHeader(line);
            params.add(pair);
        }
    }

    public Resource getResource() {
        return requestLine.getResource();
    }
}
