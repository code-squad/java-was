package com.larry.webserver;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ControllerExecutor {

    private final Set<Class<?>> controllers;

    public ControllerExecutor(Set<Class<?>> controllers) {
        this.controllers = controllers;
    }

    public ControllerExecutor(BeanPool beanPool) {
        this.controllers = beanPool.getBeans();
    }

    public String retrieveViewName(Request request) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class<?> controller : controllers) {
            MethodExecutor methodExecutor = new MethodExecutor(controller);
            if (methodExecutor.findMethod(request)) {
                return methodExecutor.getViewName(request);
            }
        }
        return "";
    }

}
