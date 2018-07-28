package com.larry.webserver;

import com.larry.webserver.annotations.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class PathFinder {

    private final Set<Class<?>> controllers;

    public PathFinder(Set<Class<?>> controllers) {
        this.controllers = controllers;
    }

    public String findMethod(Request request) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        MethodFinder finder;
        for (Class<?> controller : controllers) {
            String path1 = getControllerPath(controller);
            finder = new MethodFinder(controller, path1);
            if (finder.findMethod(request)) {
                return finder.getViewName(request);
            }
        }
        return null;
    }

    private String getControllerPath(Class<?> controller) {
        if (controller.isAnnotationPresent(RequestMapping.class)) {
            return controller.getAnnotation(RequestMapping.class).path();
        }
        return "";
    }

}
