package webserver.handlermapping;

import setting.ArgumentResolver;

import java.util.Map;
import java.util.stream.Stream;

@ArgumentResolver
public class StringMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(Class clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance() instanceof String;
    }

    @Override
    public Object resolveArgument(Class clazz, String jSessionId, Map<String, String> body) {
        return body.remove(body.keySet().iterator().next());
    }
}
