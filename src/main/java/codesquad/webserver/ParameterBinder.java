package codesquad.webserver;

import codesquad.model.Header;
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

    public static Object[] bind(Method thisMethod, Header header) throws Exception {
        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : thisMethod.getParameterTypes()) {
            Object aInstance = parameterType.newInstance();
            if (isValidForQuery(parameterType, header)) {
                args.add(bindingQuery(aInstance, header));
                continue;
            }
            if (isValidForCookie(aInstance)) {
                args.add(bindingCookie(aInstance, header));
                continue;
            }
            args.add(aInstance);
        }
        return args.toArray();
    }

    static boolean isValidForCookie(Object aInstance) {
        return aInstance instanceof HttpSession;
    }

    static boolean isValidForQuery(Class<?> parameterType, Header header) {
        List<String> fields = Arrays.stream(parameterType.getDeclaredFields())
                .map(field -> field.getName()).collect(Collectors.toList());
        return header.hasAllThoseFields(fields);
    }

    static Object bindingQuery(Object aInstance, Header header) {
        return header.bindingQuery(aInstance);
    }

    public static Object bindingCookie(Object aInstance, Header header) {
        HttpSession httpSession = (HttpSession) aInstance;
        httpSession.addCookie(header);
        return httpSession;
    }
}
