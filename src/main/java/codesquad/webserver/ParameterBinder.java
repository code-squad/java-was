package codesquad.webserver;

import codesquad.model.request.HttpRequest;
import codesquad.model.HttpSession;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinder {
    private static final Logger log = getLogger(ParameterBinder.class);
    private static final Map<MethodParameter, HandlerMethodArgumentResolver> parameterBinderHandler = new HashMap<>();

    static {
        parameterBinderHandler.put(MethodParameter.COMMON, new QueryHandlerMethodArgumentResolver());
        parameterBinderHandler.put(MethodParameter.HTTP_SESSION, new SessionHandlerMethodArgumentResovler());
    }

    public static Object[] bind(Method thisMethod, HttpRequest httpRequest) throws Exception {
        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : thisMethod.getParameterTypes()) {
            Object aInstance = parameterType.newInstance();

            HandlerMethodArgumentResolver handlerMethodArgumentResolver =
                    parameterBinderHandler.get(MethodParameter.getMethodParameter(aInstance));
            if(handlerMethodArgumentResolver.supportsParameter(parameterType, httpRequest)) {
                aInstance = handlerMethodArgumentResolver.resolveArgument(aInstance, httpRequest);
            }

            args.add(aInstance);
        }
        return args.toArray();
    }

}
