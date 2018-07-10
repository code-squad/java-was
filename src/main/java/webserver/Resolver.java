package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Resolver {

    private final Logger log = LoggerFactory.getLogger(Resolver.class);

    public void resolve(Request request, OutputStream out) throws InvocationTargetException, IllegalAccessException, IOException {
        String path = request.getPath();
        HttpMethod httpMethod = request.getHttpMethod();
        String viewFileName = null;

        DataOutputStream dos = new DataOutputStream(out);

        Method[] declaredMethods = HttpController.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            log.debug("request path : {}", path);
            log.debug("request method : {}", httpMethod);
            if (requestMapping.path().equals(path) && requestMapping.method().equals(httpMethod.toString())) {
                if (request.getParams() != null) {
                    viewFileName = (String) method.invoke(new HttpController(), request.params);
                    break;
                }
                viewFileName = (String) method.invoke(new HttpController());
                break;
            }
        }
        Response response = new Response(request, viewFileName);
        response.send(dos);
    }

}
