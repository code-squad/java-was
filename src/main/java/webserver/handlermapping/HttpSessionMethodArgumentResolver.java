package webserver.handlermapping;

import security.HttpSession;
import setting.ArgumentResolver;

import java.util.Map;

@ArgumentResolver
public class HttpSessionMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(Class clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance() instanceof HttpSession;
    }

    @Override
    public Object resolveArgument(Class clazz, String jSessionId, Map<String, String> body) {
        body.remove(clazz.getSimpleName());
        return new HttpSession(jSessionId);
    }
}
