package webserver;

import util.RequestUtils;
import util.RequestUtils.Pair;

import java.util.HashMap;
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
        params = new HashMap<>();
        if (pathAndParamsToken.size() == 2) {
            initParams(pathAndParamsToken.get(1));
        }
        path = pathAndParamsToken.get(0);
    }

    private void initParams(String paramsSource) {
        List<Pair> paramPairs = RequestUtils.splitParams(paramsSource);
        paramPairs.forEach(pair -> params.put(pair.getKey(), pair.getValue()));
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

    boolean isIncludeBody() {
        return method.isIncludeBody();
    }
}
