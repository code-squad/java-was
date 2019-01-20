package codesquad.webserver;

import codesquad.model.Header;
import codesquad.model.HttpSession;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ParameterBinder {
    private static final Logger log = getLogger(ParameterBinder.class);

    public static Object[] bind(Method thisMethod, Header header) throws Exception {
        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : thisMethod.getParameterTypes()) {
            Object aInstance = parameterType.newInstance();
            if (isValidForQuery(parameterType, header)) {
                args.add(bindingQeury(aInstance, header));
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
        for (Field declaredField : parameterType.getDeclaredFields()) {
            if (!header.hasSameFieldName(declaredField.getName())) return false;
        }
        return true;
    }

    static Object bindingQeury(Object aInstance, Header header) {
        Arrays.stream(aInstance.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set"))
                .forEach(method -> header.injectValue(aInstance, method));
        return aInstance;
    }

    public static Object bindingCookie(Object aInstance, Header header) {
        HttpSession httpSession = (HttpSession) aInstance;
        httpSession.addCookie(header);
        return httpSession;
    }
}
