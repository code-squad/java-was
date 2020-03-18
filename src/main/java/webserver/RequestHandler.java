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
      Map<String, Pair> requestHeader = requestHeader(br);
      String requestBody = "";
      if (requestHeader.containsKey("Content-Length")) {
        requestBody = requestBody(br, Integer.parseInt(requestHeader.get("Content-Length").getValue()));
      }

      PageController pc = new PageController();
      Map<String, String> response = pc.doWork(requestLine, requestHeader, requestBody);

      String statusLine = statusLine(response.get("statusCode"), response.get("message"));
      log.debug("### statusLine: {}", statusLine);
      byte[] body = responseBody(response.get("responseBodyUrl"));
      String responseHeader = responseHeader(body.length, response.get("location"));
      log.debug("### responseHeader : {}", responseHeader);

      sendResponse(dos, statusLine, responseHeader, body);
    } catch (IOException ie) {
      log.error(ie.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  /**
   * Feat : parsing 된 requestLine 을 리턴해줍니다.
   * Desc : parseRequestLine() 을 통해 method, requestUrl, protocol 로 parsing 됩니다.
   * Return : Map<String, Pair>
   */
  private Map<String, Pair> requestLine(BufferedReader br) throws Exception {
    if (!br.ready()) return new HashMap<>();

    Map<String, Pair> requestLine = new HashMap<>();
    Pair[] parsedRequestLine = HttpRequestUtils.parseRequestLine(br.readLine());
    Arrays.stream(parsedRequestLine).forEach(token -> requestLine.put(token.getKey(), token));

    return requestLine;
  }

  /**
   * Feat : parsing 된 requestHeader 를 리턴해줍니다.
   * Desc : parseHeader() 를 통해 header 에 들어 있는 값들을 parsing 합니다.
   * Return : Map<String, Pair>
   */
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

  /**
   * Feat : requestBody 를 가져옵니다.
   * Desc : contentLength 만큼 가져옵니다.
   * Return : String
   */
  private String requestBody(BufferedReader br, int contentLength) throws Exception {
    return (br.ready()) ? IOUtils.readData(br, contentLength) : null;
  }

  /**
   * Feat : statusLine 을 만듭니다.
   * Desc :
   * Return : String
   */
  private String statusLine(String statusCode, String message) {
    log.debug("### statusLine");
    return "HTTP/1.1 " + statusCode + " " + message + "\r\n";
  }

  /**
   * Feat : responseHeader 를 만듭니다.
   * Desc :
   * Return : String
   */
  private String responseHeader(int lengthOfBodyContent, String location) {
    log.debug("### responseHeader, {}", lengthOfBodyContent);

    StringBuilder sb = new StringBuilder();

    sb.append("location: ").append(location).append("\r\n");
    sb.append("Content-Type: text/html;charset=utf-8\r\n");
    sb.append("Content-Length: ").append(lengthOfBodyContent).append("\r\n");
    sb.append("\r\n");

    return sb.toString();
  }

  /**
   * Feat : responseURL 을 가지고 responseBody 를 만듭니다.
   * Desc :
   * Return : byte[]
   */
  private byte[] responseBody(String responseBodyURL) throws Exception {
    File uriFile = new File(WEBAPP_PATH + responseBodyURL);
    return Files.readAllBytes(uriFile.toPath());
  }

  /**
   * Feat : reponse 를 client 에게 보내줍니다.
   * Desc :
   * Return : void
   */
  private void sendResponse(DataOutputStream dos, String statusLine, String header, byte[] body) throws Exception {
    log.debug("### DataOutputStream");
    log.debug("### statusLine : {}", statusLine);
    dos.writeBytes(statusLine);
    dos.writeBytes(header);
    dos.write(body, 0, body.length);
    dos.flush();
  }
}
