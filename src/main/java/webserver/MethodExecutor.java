package webserver;

import webserver.annotations.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class MethodExecutor {

    public static String execute(BeanPool beanPool, Request request) throws InvocationTargetException, IllegalAccessException, InstantiationException {

        Set<Class<?>> controllers = beanPool.getControllers();

        StringBuffer sb = new StringBuffer();
        for (Class controller: controllers) {
            if (controller.isAnnotationPresent(RequestMapping.class)) {
                sb.append(((RequestMapping)controller.getAnnotation(RequestMapping.class)).path());
            }
            Method[] methods = controller.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    sb.append(method.getAnnotation(RequestMapping.class).path());
                }
                if (sb.toString().equals(request.getPath()) && method.getAnnotation(RequestMapping.class).method().equals(request.getHttpMethod().toString())) {
                    return getString(controller, request, method);
                }
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
