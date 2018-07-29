package com.larry.webserver;

import com.larry.webserver.exceptions.ControllerExecuteException;
import com.larry.webserver.exceptions.ExceptionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FrontController {

    private ControllerExecutor controllerExecutor;

    private FrontController(BeanPool beanPool) {
        controllerExecutor = new ControllerExecutor(beanPool);
    }

    public static FrontController init(BeanPool beanPool) {
        return new FrontController(beanPool);
    }

    public Response resolveRequest(Request request) throws InstantiationException, IllegalAccessException, IOException{
        try {
            return controllerExecutor.retrieveViewName(request);
        } catch (InvocationTargetException e) {
            return ExceptionHandler.handle(request, e);
        }
    }
}
