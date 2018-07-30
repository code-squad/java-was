package com.larry.webserver.mvc.controllerFlow;

import com.larry.webserver.http.Request;
import com.larry.webserver.http.RequestHandlerValue;
import com.larry.webserver.http.Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ControllerExecutor {

    private final MethodPool methodPool;

    public ControllerExecutor(MethodPool methodPool) {
        this.methodPool = methodPool;
    }

    public Response retrieveViewName(Request request) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException, NoSuchMethodException {

        RequestHandlerValue controllerAndMethod = methodPool.getMethodValue(request.getHttpMethodAndPath());
        Response response = new Response();
        return controllerAndMethod.execute(request, response);
    }

}
