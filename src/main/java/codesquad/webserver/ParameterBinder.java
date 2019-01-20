package codesquad.webserver;

import codesquad.model.Header;
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
            if (isValid(parameterType, header)) {
                args.add(binding(aInstance, header));
                continue;
            }
            args.add(aInstance);
        }
        return args.toArray();
    }

    public static boolean isValid(Class<?> parameterType, Header header) {
        for (Field declaredField : parameterType.getDeclaredFields()) {
            if (!header.hasSameFieldName(declaredField.getName())) return false;
        }
        return true;
    }

    public static Object binding(Object aInstance, Header header) {
        Arrays.stream(aInstance.getClass().getDeclaredMethods())
                .filter(method -> method.getName().startsWith("set"))
                .forEach(method -> header.injectValue(aInstance, method));
        return aInstance;
    }
}
