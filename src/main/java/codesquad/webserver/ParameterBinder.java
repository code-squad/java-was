package codesquad.webserver;

import codesquad.model.Url;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterBinder {
    public static Object[] bind(Method thisMethod, Url url) {
        for (Parameter parameter : thisMethod.getParameters()) {
            Class<?> type = parameter.getType();
        }
        return new Object[0];
    }
}
