package webserver.support.controller;

import webserver.request.RequestParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ModelInitializer {
    public static <T> Optional<T> init(RequestParameter parameter, Class<T> targetType) {
        try {
            T instance = targetType.getDeclaredConstructor().newInstance();
            List<Method> injectTargets = Arrays.stream(targetType.getDeclaredMethods()).filter(method -> method.getName().startsWith("set")).collect(toList());
            for (Method target : injectTargets) {
                target.invoke(instance, parameter.get(getInjectTargetName(target.getName())));
            }
            return Optional.of(instance);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    private static String getInjectTargetName(String methodName) {
        String temp = methodName.replace("set", "");
        return temp.substring(0, 1).toLowerCase() + temp.substring(1);
    }
}
