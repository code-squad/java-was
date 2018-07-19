package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        // 포트 할당
        int port = portAllocate(args);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            connect(listenSocket);
        }
    }

    private static void connect(ServerSocket listenSocket) throws IOException {
        // 블록
        Socket connection;
        while ((connection = listenSocket.accept()) != null) {
            RequestHandler requestHandler = new RequestHandler(connection);
            requestHandler.start();
        }
    }

    private static int portAllocate(String[] args) {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            // main 인자로 받는다
            port = Integer.parseInt(args[0]);
        }
        return port;
    }
}
