package codesquad.webserver;

import codesquad.model.request.HttpRequest;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinder {
    private static final Logger log = getLogger(ParameterBinder.class);
    private static final List<HandlerMethodArgumentResolver> parameterBinderHandler = new ArrayList<>();

    static {
        parameterBinderHandler.add(new QueryHandlerMethodArgumentResolver());
        parameterBinderHandler.add(new SessionHandlerMethodArgumentResovler());
    }

    public static Object[] bind(Method thisMethod, HttpRequest httpRequest) throws Exception {
        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : thisMethod.getParameterTypes()) {
            args.add(getObject(httpRequest, parameterType));
        }
        return args.toArray();
    }

    private static Object getObject(HttpRequest httpRequest, Class<?> parameterType) throws Exception {
        Object aInstance = parameterType.newInstance();
        for (HandlerMethodArgumentResolver resolver : parameterBinderHandler) {
            if(resolver.supportsParameter(parameterType, httpRequest)) {
                aInstance = resolver.resolveArgument(aInstance, httpRequest);
                break;
            }
        }
        return aInstance;
    }

}
