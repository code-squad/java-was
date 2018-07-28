package com.larry.webserver;

import com.larry.webserver.annotations.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodFinder {

    private final Logger log = LoggerFactory.getLogger(MethodFinder.class);

    private Class<?> controller;
    private String controllerMappedPath;
    private Method executeMethod;

    public MethodFinder(Class<?> controller, String path1) {
        this.controller = controller;
        this.controllerMappedPath = path1;
    }

    public boolean findMethod(Request request) {
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            String methodPath = "";
            if (method.isAnnotationPresent(RequestMapping.class)) {
                methodPath = method.getDeclaredAnnotation(RequestMapping.class).path();
            }
            String fullPath = controllerMappedPath + methodPath;
            log.info("full path : {}", fullPath);
            if (fullPath.equals(request.getPath()) && HttpMethod.valueOf(method.getAnnotation(RequestMapping.class).method()).equals(request.getHttpMethod())) {
                executeMethod = method;
                return true;
            }
        }
        return false;
    }

    public String getViewName(Request request) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (request.getParams() != null) {
            return (String) executeMethod.invoke(controller.newInstance(), request.getParams());
        }
        return (String) executeMethod.invoke(controller.newInstance());
    }
}
