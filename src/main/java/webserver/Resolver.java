package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpController;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

public class Resolver {

    private final Logger log = LoggerFactory.getLogger(Resolver.class);

    public void resolve(Request request, OutputStream out) throws InvocationTargetException, IllegalAccessException, IOException {
        String path = request.getPath();
        String httpMethod = request.getHttpMethod();
        String viewFileName = null;
        // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        DataOutputStream dos = new DataOutputStream(out);

        // TODO 컨트롤러 맵핑 후 뷰를 받아오는 과정이 필요함
        Method[] declaredMethods = HttpController.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            log.debug("request path : {}", path);
            log.debug("request method : {}", httpMethod);
            if (requestMapping.path().equals(path) && requestMapping.method().equals(httpMethod)) {
                viewFileName = (String) method.invoke(new HttpController());
            }
        }
        byte[] body = Files.readAllBytes(new File("src/main/resources/static/" + viewFileName).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
