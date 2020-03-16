package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class RequestHandler extends Thread {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private Socket connection;

  public RequestHandler(Socket connectionSocket) {
    this.connection = connectionSocket;
  }

  public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());

    try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
      // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
      DataOutputStream dos = new DataOutputStream(out);
//      byte[] body = "Hello World".getBytes();
      //            log.debug("### run");

      BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
      String line;

      //      while (!"".equals(line = buffer.readLine())) {
      //        if (line == null) break;
      //        log.debug("### : {}", line);
      //      }

      List<String> inputList = buffer.lines().collect(Collectors.toList());
      String[] requestLine = inputList.get(0).split(" ");
      String requestUrl =requestLine[1];

      log.debug("### : {}", requestUrl);

      File requestFile = new File("./webapp" + requestUrl);
      byte[] body = Files.readAllBytes(requestFile.toPath());

      log.debug("### body :  {}", new String(body));
      log.debug("### : end of request");

      response200Header(dos, body.length);
      responseBody(dos, body);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
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
