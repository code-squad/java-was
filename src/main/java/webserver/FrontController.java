package webserver;

import web.HttpController;
import webserver.exceptions.ExceptionHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FrontController {

    public Response resolveRequest(Request request) throws IllegalAccessException, IOException, InstantiationException {

        try {
            String viewFileName;
            viewFileName = ControllerExecutor.execute(HttpController.class, request);
            byte[] viewBody = ViewResolver.resolve(viewFileName);
            return new Response(request, viewBody, viewFileName);
        } catch (InvocationTargetException e) {
            ExceptionHandler.handle(e);
        }
        return null;
    }
}
