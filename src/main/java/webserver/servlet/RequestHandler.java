package webserver.servlet;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.HttpSession;
import util.HttpRequestFactory;
import util.HttpResponseFactory;
import webserver.viewresolver.ClientModel;
import webserver.handlermapping.HandlerMapping;
import webserver.viewresolver.Model;

public class RequestHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest httpRequest = HttpRequestFactory.of(br);
            logger.debug("Request Entity 읽은 후, 생성 : {}", httpRequest.toString());

            String view = HandlerMapping.processHandler(httpRequest.getMapping(), httpRequest.getBody()
                    , httpRequest.obtainHeader("JSESSIONID"));
            logger.debug("Return view : {}", view);
            /* [개선점] AOP 를 통해 자동으로 로그인 확인 유무를 체크하길 원함!
                세션에 본인이 등록되었는지 확인하여 logined=true, or false 설정
            */
            String logined = "logined=false";
            String jSessionId = httpRequest.obtainHeader("JSESSIONID");
            if(HttpSession.isSession(jSessionId)) {
                logined = "logined=true";
            }
            httpRequest.addHeader("Cookie", logined);

            /* Model 관련 로직 */
            ClientModel clientModel = null;
            if(Model.isModel(jSessionId)) {
                clientModel = Model.obtainModel(jSessionId);
            }

            HttpResponse responseEntity = HttpResponseFactory.of(httpRequest, view, clientModel)
                    .responseHeader(dos, httpRequest.isResource()).responseBody(dos);
            logger.debug("ResponseEntity : {}", responseEntity.toString());

        } catch (IOException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
    }
}
