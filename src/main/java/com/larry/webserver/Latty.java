package com.larry.webserver;

import com.larry.webserver.annotations.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.larry.webserver.annotations.LattyFramework;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.ServerSocket;
import java.net.Socket;

public class Latty {

    private static FrontController frontController;
    private static final String CONTROLLER_PATH = "com/larry/web";

    private static final Logger log = LoggerFactory.getLogger(Latty.class);

    public static final int DEFAULT_PORT = 8080;
    public static final String HOST = "http://localhost";

    public static void run(Class<?> mainClazz, String[] args) {
        for (Annotation annotation : mainClazz.getAnnotations()) {
            if (annotation instanceof LattyFramework) {
                initFrontController(new ControllerPool(new BeanFinder(CONTROLLER_PATH, Controller.class)));
                connectSocket(args);
            }
        }
    }

    private static void initFrontController(ControllerPool controllerPool) {
        frontController = FrontController.init(controllerPool);
    }

    private static void connectSocket(String[] args) {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, frontController);
                requestHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
