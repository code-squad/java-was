package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerExecutor {

    public static String execute(Class<?> clazz, Request request) throws InvocationTargetException, IllegalAccessException, InstantiationException {

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping.path().equals(request.getPath()) && requestMapping.method().equals(request.getHttpMethod().toString())) {
                return getString(clazz, request, method);
            }
        }
        throw new RuntimeException("잘못된 요청입니다.");
    }

    private static String getString(Class<?> clazz, Request request, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (request.getParams() != null) {
            return (String) method.invoke(clazz.newInstance(), request.getParams());
        }
        return (String) method.invoke(clazz.newInstance());
    }
}
