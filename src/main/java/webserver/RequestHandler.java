package webserver;

import java.io.*;
import java.net.Socket;

import db.DataBase;
import model.RequestEntity;
import model.ResponseEntity;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

            //requestEntity = HandlerMapping.processCookie(requestEntity);
            requestEntity = HandlerMapping.processRequest(requestEntity);
            logger.debug("Request Entity After Process : {}", requestEntity.toString());

            ResponseEntity responseEntity = ResponseEntityFactory.of(requestEntity).responseHeader(dos).responseBody(dos);
            logger.debug("ResponseEntity : {}", responseEntity.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
