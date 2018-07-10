package webserver;

import util.RequestUtils;

import java.util.List;
import java.util.Map;

import static util.RequestUtils.splitPathAndParams;

class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final Map<String, String> params;

    RequestLine(String requestLine) {
        List<String> lines = RequestUtils.splitRequestLine(requestLine);
        method = HttpMethod.get(lines.get(0));

        List<String> pathAndParamsToken = splitPathAndParams(lines.get(1));
        path = pathAndParamsToken.get(0);
        params = RequestUtils.splitQueryString(pathAndParamsToken.stream().filter(o -> pathAndParamsToken.indexOf(o) > 0).findFirst().orElse(null));
    }

    String getMethod() {
        return method.name();
    }

    String getPath() {
        return path;
    }

    String getParam(String key) {
        if (params.isEmpty() || !params.containsKey(key)) {
            throw new IllegalArgumentException();
        }
        return params.get(key);
    }
}
