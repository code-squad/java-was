package webserver.handlermapping;

import setting.ArgumentResolver;
import webserver.viewresolver.Model;

import java.util.Map;

@ArgumentResolver
public class ModelMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Class clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance() instanceof Model;
    }

    @Override
    public Object resolveArgument(Class clazz, String jSessionId, Map<String, String> body) {
        body.remove(clazz.getSimpleName());
        return new Model(jSessionId);
    }
}
