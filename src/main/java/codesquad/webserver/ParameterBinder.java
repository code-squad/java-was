package codesquad.webserver;

import codesquad.model.HttpRequest;
import codesquad.model.HttpSession;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinder {
    private static final Logger log = getLogger(ParameterBinder.class);

    public static Object[] bind(Method thisMethod, HttpRequest httpRequest) throws Exception {
        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : thisMethod.getParameterTypes()) {
            Object aInstance = parameterType.newInstance();
            if (isValidForQuery(parameterType, httpRequest)) {
                args.add(bindingQuery(aInstance, httpRequest));
                continue;
            }
            if (isValidForCookie(aInstance)) {
                args.add(bindingCookie(aInstance, httpRequest));
                continue;
            }
            args.add(aInstance);
        }
        return args.toArray();
    }

    static boolean isValidForCookie(Object aInstance) {
        return aInstance instanceof HttpSession;
    }

    static boolean isValidForQuery(Class<?> parameterType, HttpRequest httpRequest) {
        List<String> fields = Arrays.stream(parameterType.getDeclaredFields())
                .map(field -> field.getName()).collect(Collectors.toList());
        return httpRequest.hasAllThoseFields(fields);
    }

    static Object bindingQuery(Object aInstance, HttpRequest httpRequest) {
        return httpRequest.bindingQuery(aInstance);
    }

    public static Object bindingCookie(Object aInstance, HttpRequest httpRequest) {
        HttpSession httpSession = (HttpSession) aInstance;
        httpSession.addCookie(httpRequest);
        return httpSession;
    }
}
