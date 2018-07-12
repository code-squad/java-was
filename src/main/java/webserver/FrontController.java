package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class FrontController {

    public Response resolveRequest(Request request) throws InvocationTargetException, IllegalAccessException, IOException, InstantiationException {

        String viewFileName;
        viewFileName = ControllerExecutor.execute(HttpController.class, request);
        byte[] viewBody = ViewResolver.resolve(viewFileName);
        return new Response(request, viewBody, viewFileName);
    }

}
