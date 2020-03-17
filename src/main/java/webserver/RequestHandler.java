package webserver;

import Controller.WelcomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler extends Thread {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
  private final String WEBAPP_PATH = System.getProperty("user.dir") + "/webapp";
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
      String[] tokens = new String[3];
      String line = null;

//      while (!"".equals(line = br.readLine()) || line == null) {
//        if (line.startsWith("GET")) {
//          tokens = line.split(" ");
//        }
//      }

      List<String> requestHeaders = requestHeader(br);
      log.debug("### requestHeader : {}", requestHeaders);
      List<String> requestBodys = requestBody(br);
      log.debug("### requestBody : {}", requestBodys);

//
//      WelcomeController wc = new WelcomeController();
//      wc.doWork(tokens);
//
//      File uriFile = new File(WEBAPP_PATH + tokens[1]);
//      byte[] body = Files.readAllBytes(uriFile.toPath());
//
//      response200Header(dos, body.length);
//      responseBody(dos, body);
    } catch (IOException ie) {
      log.error(ie.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
    try {
      log.debug("### response200Header, {}", lengthOfBodyContent);
      dos.writeBytes("HTTP/1.1 200 OK \r\n");
      dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
      dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
      dos.writeBytes("\r\n");

    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private List<String> requestHeader(BufferedReader br) throws Exception {
    String line = null;
    List<String> requestHeaders = new ArrayList<>();

    while (!"".equals(line = br.readLine()) || line == null) {
      requestHeaders.add(line);
    }

    return requestHeaders;
  }

  private List<String> requestBody(BufferedReader br) throws Exception {
    String line = null;
    List<String> requestBody = new ArrayList<>();

    while (!"".equals(line = br.readLine()) || line == null) {
      requestBody.add(line);
    }

    return requestBody;
  }

  private void responseBody(DataOutputStream dos, byte[] body) {
    try {
      log.debug("### DataOutputStream");
      dos.write(body, 0, body.length);
      dos.flush();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
