package webserver.request;

import exception.NotFoundRequestParameterException;
import util.RequestUtils;
import webserver.HttpMethod;

import java.util.List;

import static util.RequestUtils.splitPathAndParams;

class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final RequestParameter parameter;

    RequestLine(String requestLine) {
        List<String> lines = RequestUtils.splitRequestLine(requestLine);
        method = HttpMethod.get(lines.get(0));

        List<String> pathAndParamsToken = splitPathAndParams(lines.get(1));
        path = pathAndParamsToken.get(0);
        parameter = new RequestParameter(RequestUtils.splitQueryString(pathAndParamsToken.stream().filter(o -> pathAndParamsToken.indexOf(o) > 0).findFirst().orElse(null)));
    }

    String getMethod() {
        return method.name();
    }

    String getPath() {
        return path;
    }

    String getParam(String key) {
        if (parameter.isEmpty() || !parameter.containsKey(key)) {
            throw new NotFoundRequestParameterException();
        }
        return parameter.get(key);
    }

    RequestParameter getParameters() {
        return parameter;
    }

    String getFirstPath() {
        int secondSlash = path.replaceFirst("/", "").indexOf("/") + 1;
        if (secondSlash == 0) {
            return path;
        }
        return path.substring(0, secondSlash).toLowerCase();
    }

    String getUriExcludeParams() {
        return path.split("\\?")[0];
    }

    boolean isMatchMethod(HttpMethod method) {
        return this.method == method;
    }
}
