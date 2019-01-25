package codesquad.webserver;

import codesquad.model.request.HttpRequest;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(Class<?> parameterType, HttpRequest httpRequest) throws Exception;

    Object resolveArgument(Object aInstance, HttpRequest httpRequest);
}
