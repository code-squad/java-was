package webserver.request;

import com.google.common.collect.Maps;
import exception.NotFoundRequestParameterException;

import java.util.Map;
import java.util.Objects;

public class RequestParameter {

    private final Map<String, String> params;

    public RequestParameter(Map<String, String> params) {
        if (Objects.isNull(params) || params.isEmpty()) {
            this.params = Maps.newHashMap();
            return;
        }
        this.params = params;
    }

    boolean isEmpty() {
        return params.isEmpty();
    }

    boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public String get(String key) {
        if (!containsKey(key)) {
            throw new NotFoundRequestParameterException();
        }
        return params.get(key);
    }
}
