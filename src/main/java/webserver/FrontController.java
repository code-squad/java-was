package webserver;

import webserver.annotations.Controller;
import webserver.exceptions.ExceptionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FrontController {

    public Response resolveRequest(Request request) throws IllegalAccessException, IOException, InstantiationException {

        try {
            BeanPool beanPool = new ControllerPool(new BeanFinder("", Controller.class));
            String viewFileName = MethodExecutor.execute(beanPool, request);
            byte[] viewBody = ViewResolver.resolve(viewFileName);
            return new Response(request, viewBody, viewFileName);
        } catch (InvocationTargetException e) {
            ExceptionHandler.handle(e);
        }
        return null;
    }
}
