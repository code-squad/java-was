package com.larry.webserver.mvc.controllerFlow;

import com.google.common.collect.Maps;
import com.larry.webserver.annotations.RequestMapping;
import com.larry.webserver.http.HttpMethod;
import com.larry.webserver.http.RequestHandlerKey;
import com.larry.webserver.http.RequestHandlerValue;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MethodFinder {

    private static final Logger log = LoggerFactory.getLogger(MethodFinder.class);

    private Map<RequestHandlerKey, RequestHandlerValue> methodMap;

    public MethodFinder(String defaultPath, Class<? extends Annotation> annotation) {
        this.methodMap = makeMap(defaultPath, annotation);
    }

    private Map<RequestHandlerKey, RequestHandlerValue> makeMap(String defaultPath, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(defaultPath);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(annotation);
        Map<RequestHandlerKey, RequestHandlerValue> result = new HashMap<>();
        log.info("controllers : {}", controllers);
        for (Class<?> controller : controllers) {
            log.info("scan controller : {}", controller);
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                log.info("method array {}", method);
                RequestMapping controllerAnnotation = controller.getAnnotation(RequestMapping.class);
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                String path = controllerAnnotation.path() + methodAnnotation.path();
                HttpMethod httpMethod = methodAnnotation.method();
                log.info("path and method {}, {}", path, httpMethod);
                result.put(new RequestHandlerKey(path, httpMethod), new RequestHandlerValue(controller, method));
            }
        }
        return result;
    }

    public RequestHandlerValue findValue(RequestHandlerKey httpMethodAndPath) {
        return methodMap.get(httpMethodAndPath);
    }
}
