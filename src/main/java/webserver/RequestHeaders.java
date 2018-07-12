package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class RequestHeaders {
    private final Map<String, String> parameters;

    public RequestHeaders(BufferedReader reader) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        while (true) {
            String line = reader.readLine();
            if ("".equals(line) || line == null) {
                break;
            }
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            parameters.put(pair.getKey(), pair.getValue());
        }
        this.parameters = parameters;
    }

    public String getHeader(String key) {
        return parameters.get(key);
    }

    public boolean containsHeader(String key) {
        return parameters.containsKey(key);
    }

    @Override
    public String toString() {
        return parameters.entrySet().stream()
                .map(Map.Entry::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
