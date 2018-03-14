package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, Controller> controllers;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        // 사용자 요청이 있을 때마다 매번 Map을 초기화지 않기 위해 한번만 처리.
        controllers = RequestMapping.createController();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // request
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            // response
            // create user(get, post)

            if(controllers.get(httpRequest.getPath()) == null){
                String contentType = "text/html";
                if(httpRequest.isStyleSheet()){
                    contentType = "text/css";
                }
                httpResponse.forward(contentType, httpResponse.readFileToByte(httpRequest.getPath()));
                return;
            }

            controllers.get(httpRequest.getPath()).service(httpRequest, httpResponse);


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
