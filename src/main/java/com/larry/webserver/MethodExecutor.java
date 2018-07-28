package com.larry.webserver;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class MethodExecutor {

    private PathFinder pathFinder;

    public String execute(BeanPool beanPool, Request request) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Set<Class<?>> controllers = beanPool.getBeans();
        pathFinder = new PathFinder(controllers);
        return pathFinder.findMethod(request);
    }
}
