package com.larry.webserver.mvc.controllerFlow;

import com.larry.webserver.exceptions.ExceptionHandler;
import com.larry.webserver.http.Request;
import com.larry.webserver.http.Response;

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

    public Response resolveRequest(Request request) throws InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException {
        try {
            return controllerExecutor.retrieveViewName(request);
        } catch (InvocationTargetException e) {
            return ExceptionHandler.handle(request, e);
        }
    }
}
