package webserver;

import java.io.*;
import java.net.Socket;

import db.DataBase;
import model.RequestEntity;
import model.ResponseEntity;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HandlerMapping;
import util.RequestEntityFactory;
import util.ResponseEntityHeaderFactory;

public class RequestHandler extends Thread implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            RequestEntity requestEntity = RequestEntityFactory.of(br);
            logger.debug("Request Entity : {}", requestEntity.toString());

            Object savedObject = HandlerMapping.saveData(requestEntity);
            if(savedObject instanceof User) {
                User user = (User) savedObject;
                logger.debug("Saved User : {}", user.toString());
                DataBase.addUser(user);
            }

            ResponseEntity responseHeader = ResponseEntityHeaderFactory.of(requestEntity).responseHeader(dos).responseBody(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        logger.debug("소켓 종료!");
    }
}
