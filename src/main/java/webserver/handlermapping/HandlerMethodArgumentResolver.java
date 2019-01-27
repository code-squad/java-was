package webserver.handlermapping;

import java.util.Map;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(Class clazz) throws IllegalAccessException, InstantiationException;

    Object resolveArgument(Class clazz, String jSessionId, Map<String, String> body);

    Class identification();
}
