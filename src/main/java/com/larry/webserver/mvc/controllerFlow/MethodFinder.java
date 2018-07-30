package com.larry.webserver.mvc.controllerFlow;

import com.google.common.collect.Maps;
import com.larry.webserver.annotations.RequestMapping;
import com.larry.webserver.http.HttpMethod;
import com.larry.webserver.http.RequestHandlerKey;
import com.larry.webserver.http.RequestHandlerValue;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class MethodFinder {

    private Map<RequestHandlerKey, RequestHandlerValue> methodMap;

    public MethodFinder(String defaultPath, Class<? extends Annotation> annotation) {
        this.methodMap = makeMap(defaultPath, annotation);
    }

    private Map<RequestHandlerKey, RequestHandlerValue> makeMap(String defaultPath, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(defaultPath);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(annotation);
        Map<RequestHandlerKey, RequestHandlerValue> result = Maps.newHashMap();

        for (Class<?> controller : controllers) {
            Method[] methods = controller.getMethods();
            for (Method method : methods) {
                RequestMapping controllerAnnotation = controller.getAnnotation(RequestMapping.class);
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                String path = controllerAnnotation.path() + methodAnnotation.path();
                HttpMethod httpMethod = methodAnnotation.method();
                result.put(new RequestHandlerKey(path, httpMethod), new RequestHandlerValue(controller, method));
            }
        }
        return result;
    }

    public RequestHandlerValue findValue(RequestHandlerKey httpMethodAndPath) {
        return methodMap.get(httpMethodAndPath);
    }
}
