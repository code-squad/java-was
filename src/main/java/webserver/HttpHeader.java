package webserver;

import com.google.common.base.Strings;
import exception.NotFoundHeaderException;
import util.RequestUtils;
import util.RequestUtils.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpHeader {
    private final Map<String, String> header;

    public HttpHeader() {
        this.header = new HashMap<>();
    }

    public HttpHeader add(String headerSource) {
        Pair result = parseHeader(headerSource);
        if (!Objects.isNull(result)) {
            add(result.getKey(), result.getValue());
        }
        return this;
    }

    public HttpHeader add(String key, String value) {
        if (!isExistHeader(key)) {
            header.put(key, value);
        }
        return this;
    }

    public String getHeader(String key) {
        if (!isExistHeader(key)) {
            throw new NotFoundHeaderException();
        }
        return header.get(key);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(header);
    }

    public boolean isExistHeader(String key) {
        return header.containsKey(key);
    }

    private Pair parseHeader(String headerSource) {
        if (Strings.isNullOrEmpty(headerSource)) {
            return null;
        }
        return RequestUtils.splitHeader(headerSource).orElse(null);
    }
}
