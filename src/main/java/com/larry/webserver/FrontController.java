package com.larry.webserver;

import com.larry.webserver.exceptions.ExceptionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FrontController {

    private BeanPool beanPool;

    private FrontController (BeanPool beanPool) {
        this.beanPool = beanPool;
    }

    public static FrontController init(BeanPool beanPool) {
        return new FrontController(beanPool);
    }

    public Response resolveRequest(Request request) throws IllegalAccessException, IOException, InstantiationException {
        try {
            String viewFileName = new MethodExecutor().execute(beanPool, request);
            byte[] viewBody = ViewResolver.resolve(viewFileName);
            return new Response(request, viewBody, viewFileName);
        } catch (InvocationTargetException e) {
            ExceptionHandler.handle(e);
        }
        return null;
    }
}
