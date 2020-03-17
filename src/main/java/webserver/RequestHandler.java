package webserver;

import Controller.PageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.Pair;

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

      Map<String, Pair> requestLine = requestLine(br);
      log.debug("### requestLine : {}", requestLine);
      Map<String, Pair> requestHeader = requestHeader(br);
      log.debug("### requestHeader : {}", requestHeader);

      String requestBody = "";
      if (requestHeader.containsKey("Content-Length")) {
        log.debug("### Content-Length : {}", requestHeader.get("Content-Length").getValue());
        requestBody = requestBody(br
            , Integer.parseInt(requestHeader.get("Content-Length").getValue()));
        log.debug("### requestBody : {}", requestBody);
      }

      PageController pc = new PageController();
      String redirectUrl = pc.doWork(requestLine, requestBody);

      File uriFile = new File(WEBAPP_PATH + redirectUrl);
      byte[] body = Files.readAllBytes(uriFile.toPath());

      response200Header(dos, body.length);
      responseBody(dos, body);
    } catch (IOException ie) {
      log.error(ie.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  /**
   * @param br
   * @return
   * @throws Exception
   */

  private Map<String, Pair> requestLine(BufferedReader br) throws Exception {
    if (!br.ready()) return new HashMap<>();

    Map<String, Pair> requestLine = new HashMap<>();
    Pair[] parsedRequestLine = HttpRequestUtils.parseRequestLine(br.readLine());
    Arrays.stream(parsedRequestLine).forEach(token -> requestLine.put(token.getKey(), token));

    return requestLine;
  }

  private Map<String, Pair> requestHeader(BufferedReader br) throws Exception {
    if (!br.ready()) return new HashMap<>();

    String line;
    Map<String, Pair> requestHeader = new HashMap<>();
    while (br.ready()) {
      if ("".equals(line = br.readLine())) break;

      Pair token = HttpRequestUtils.parseHeader(line);
      requestHeader.put(token.getKey(), token);
    }

    return requestHeader;
  }

  private String requestBody(BufferedReader br, int contentLength) throws Exception {
    return (br.ready()) ? IOUtils.readData(br, contentLength) : null;
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
