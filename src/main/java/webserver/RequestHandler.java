package webserver;

import java.io.*;
import java.net.Socket;

import db.DataBase;
import model.RequestEntity;
import model.ResponseEntity;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.HttpSession;
import util.RequestEntityFactory;
import util.ResponseEntityFactory;

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
            RequestEntity requestEntity = RequestEntityFactory.of(br);
            logger.debug("Request Entity 읽은 후, 생성 : {}", requestEntity.toString());

            String view = HandlerMapping.processHandler(requestEntity.getMapping(), requestEntity.getBody(), requestEntity.getJsessionId());
            logger.debug("Return view : {}", view);
            /* [질문] AOP 를 통해 자동으로 로그인 확인 유무를 체크하길 원함!
                세션에 본인이 등록되었는지 확인하여 logined=true, or false 설정
            */
            String logined = "logined=false";
            String jSessionId = requestEntity.obtainParamElement("JSESSIONID");
            if(HttpSession.isSession(jSessionId)) {
                logined = "logined=true";
            }
            requestEntity.addHeader("Cookie", logined);

            /* Model 관련 로직 */
            ClientModel clientModel = null;
            if(Model.isModel(jSessionId)) {
                clientModel = Model.obtainModel(jSessionId);
            }

            ResponseEntity responseEntity = ResponseEntityFactory.of(requestEntity, view, clientModel)
                    .responseHeader(dos, requestEntity.isResource()).responseBody(dos);
            logger.debug("ResponseEntity : {}", responseEntity.toString());

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
