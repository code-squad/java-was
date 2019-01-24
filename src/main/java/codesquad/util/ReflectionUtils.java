package codesquad.util;

import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtils {

    public static String getFieldName(String methodName) {
        String setterName = methodName.substring(3);
        return setterName.substring(0, 1).toLowerCase() + setterName.substring(1);
    }

    public static void injectValue(Object aInstance, Method method, Map<String, String> queryValue) {
        String setterName = method.getName().substring(3);
        String fieldName = setterName.substring(0, 1).toLowerCase() + setterName.substring(1);
        try {
            method.invoke(aInstance, queryValue.get(fieldName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
