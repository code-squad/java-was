package model;

import webserver.WebServer;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

public class HttpResponse extends HttpTemplate {

  String startLine;
  String header;
  byte[] body;
  private Map<String, String> response;

  public HttpResponse(Map<String, String> response) throws Exception {
    this.response = response;
    this.startLine = statusLine();
    this.body = responseBody();
    this.header = responseHeader();
  }

  /**
   * Feat : statusLine 을 만듭니다.
   * Desc :
   * Return : Map<String, String>
   */
  private String statusLine() {
    StringBuilder sb = new StringBuilder();

    sb.append(response.get("protocol")).append(" ");
    sb.append(response.get("statusCode")).append(" ");
    sb.append(response.get("message")).append("\r\n");

    return sb.toString();
  }

  /**
   * Feat : responseURL 을 가지고 responseBody 를 만듭니다.
   * Desc :
   * Return : byte[]
   */
  private byte[] responseBody() throws Exception {
    File uriFile = new File(WebServer.getWebappPath() + response.get("responseBodyUrl"));
    return Files.readAllBytes(uriFile.toPath());
  }

  /**
   * Feat : responseHeader 를 만듭니다.
   * Desc :
   * Return : String
   */
  private String responseHeader() {
    StringBuilder sb = new StringBuilder();

    sb.append("responseBodyUrl").append(response.get("responseBodyUrl")).append("\r\n");
    sb.append("location: ").append(response.get("location")).append("\r\n");
    sb.append("Set-Cookie: ").append(response.get("Set-Cookie")).append("\r\n");
    String contentType = response.get("contentType");
    if (contentType.startsWith("text/html")) {
      sb.append("Content-Type: text/html;charset=utf-8\r\n");
    } else if (contentType.startsWith("text/css")) {
      sb.append("Content-Type: text/css\r\n");
    } else {
      sb.append("Content-Type: text/html;charset=utf-8\r\n");
    }

    sb.append("Content-Length: ").append(body.length).append("\r\n");
    sb.append("\r\n");

    return sb.toString();
  }

  public String getHeader() {
    return this.header;
  }

  public byte[] getBody() {
    return this.body;
  }

  public String getStartLine() {
    return this.startLine;
  }
}
