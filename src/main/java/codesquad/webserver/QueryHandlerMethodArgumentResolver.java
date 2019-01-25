package codesquad.webserver;

import codesquad.model.request.HttpRequest;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class QueryHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger log = getLogger(QueryHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(Class<?> parameterType, HttpRequest httpRequest) {
        List<String> fields = Arrays.stream(parameterType.getDeclaredFields())
                .map(field -> field.getName()).collect(Collectors.toList());
        return httpRequest.hasAllThoseFields(fields);
    }

    @Override
    public Object resolveArgument(Object aInstance, HttpRequest httpRequest) {
        return httpRequest.bindingQueryValue(aInstance);
    }

}
