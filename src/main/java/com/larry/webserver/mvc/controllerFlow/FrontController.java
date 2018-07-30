package com.larry.webserver.mvc.controllerFlow;

import com.larry.webserver.exceptions.ExceptionHandler;
import com.larry.webserver.http.Request;
import com.larry.webserver.http.RequestHandlerValue;
import com.larry.webserver.http.Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FrontController {

    private MethodPool methodPool;

    private FrontController(MethodPool methodPool) {
        this.methodPool = methodPool;
    }

    public static FrontController init(MethodPool methodPool) {
        return new FrontController(methodPool);
    }

    public Response resolveRequest(Request request) throws InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException {
        try {
            RequestHandlerValue controllerAndMethod = methodPool.getMethodValue(request.getHttpMethodAndPath());
            Response response = new Response();
            return controllerAndMethod.execute(request, response);

        } catch (InvocationTargetException e) {
            return ExceptionHandler.handle(request, e);
        }
    }
}
