package webserver;

import java.util.Map;
import java.util.stream.Collectors;


public class RequestHeaders {
    private final Map<String, String> parameters;

    public RequestHeaders(Map<String, String> parameters) {
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
