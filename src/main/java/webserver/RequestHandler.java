package webserver;

import Controller.PageController;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler extends Thread {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
  private Socket connection;

  public RequestHandler(Socket connectionSocket) {
    this.connection = connectionSocket;
  }

  public void run() {
    log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

    try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
      // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
      log.debug("### run");

      DataOutputStream dos = new DataOutputStream(out);
      BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

      HttpRequest httpRequest = new HttpRequest(br);

      PageController pc = new PageController();
      Map<String, String> response =
          pc.doWork(httpRequest.getStartLine(), httpRequest.getHeader(), httpRequest.getBody());

      HttpResponse httpResponse = new HttpResponse(response);
      //      sendResponse(dos, statusLine, responseHeader, body);
    } catch (IOException ie) {
      log.error(ie.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
