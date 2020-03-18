package webserver;

import Controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

  private static final Logger log = LoggerFactory.getLogger(WebServer.class);
  private static final int DEFAULT_PORT = 8080;

  public static void main(String args[]) throws Exception {
    int port = 0;
    if (args == null || args.length == 0) {
      port = DEFAULT_PORT;
    } else {
      port = Integer.parseInt(args[0]);
    }

    UserController.create("userId=choi1&password=1111&name=choiby1&email=choi1%40asd.com");
    UserController.create("userId=choi2&password=1111&name=choiby2&email=choi2%40asd.com");
    UserController.create("userId=hamill1&password=2222&name=hamill1n&email=hamill1%40asd.com");
    UserController.create("userId=hamill2&password=2222&name=hamill1n&email=hamill2%40asd.com");

    // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.

    try (ServerSocket listenSocket = new ServerSocket(port)) {
      log.info("Web Application Server started {} port.", port);

      // 클라이언트가 연결될때까지 대기한다.
      Socket connection;
      while ((connection = listenSocket.accept()) != null) {
        RequestHandler requestHandler = new RequestHandler(connection);
        requestHandler.start();
      }
    }
  }
}
