package com.larry.webserver;

import com.larry.webserver.annotations.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodExecutor {

    private final Logger log = LoggerFactory.getLogger(MethodExecutor.class);

    private Class<?> controller;
    private Method executeMethod;

    public MethodExecutor(Class<?> controller) {
        this.controller = controller;
    }

    public boolean findMethod(Request request) {
        String controllerMappedPath = getControllerPath(controller);
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            String methodPath = null;
            if (method.isAnnotationPresent(RequestMapping.class)) {
                methodPath = method.getDeclaredAnnotation(RequestMapping.class).path();
            }
            String fullPath = controllerMappedPath + methodPath;
            log.info("full path : {}", fullPath);
            if (isMatchedMethod(request, method, fullPath)) {
                executeMethod = method;
                return true;
            }
        }
        return false;
    }

    private boolean isMatchedMethod(Request request, Method method, String fullPath) {
        return fullPath.equals(request.getPath()) && HttpMethod.valueOf(method.getAnnotation(RequestMapping.class).method()).equals(request.getHttpMethod());
    }

    public String getViewName(Request request) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (request.getParams() != null) {
            return (String) executeMethod.invoke(controller.newInstance(), request.getParams());
        }
        return (String) executeMethod.invoke(controller.newInstance());
    }

    private String getControllerPath(Class<?> controller) {
        if (controller.isAnnotationPresent(RequestMapping.class)) {
            return controller.getAnnotation(RequestMapping.class).path();
        }
        return "";
    }
}
