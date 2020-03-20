package model;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpTemplate {

  public HttpRequest(BufferedReader br) throws Exception {
    this.br = br;
    this.startLine = requestLine();
    this.header = requestHeader();
    this.body = requestBody();

    //      Map<String, HttpRequestUtils.Pair> requestLine = requestLine(br);
    //      Map<String, HttpRequestUtils.Pair> requestHeader = requestHeader(br);
    //      String requestBody = "";
    //      if (requestHeader.containsKey("Content-Length")) {
    //        requestBody = requestBody(br, Integer.parseInt(requestHeader.get("Content-Length").getValue()));
    //      }
  }

  /**
   * Feat : parsing 된 requestLine 을 리턴해줍니다.
   * Desc : parseRequestLine() 을 통해 method, requestUrl, protocol 로 parsing 됩니다.
   * Return : Map<String, String>
   */
  private Map<String, String> requestLine() throws Exception {
    if (!br.ready()) return new HashMap<>();
    String line = br.readLine();

    Map<String, String> requestLine = new HashMap<>();
    String[] splitedLine = line.split(" ");

    requestLine.put("method", splitedLine[0]);
    requestLine.put("requestUrl", splitedLine[1]);
    requestLine.put("protocol", splitedLine[2]);

    return requestLine;
  }

  /**
   * Feat : parsing 된 requestHeader 를 리턴해줍니다.
   * Desc :
   * Return : Map<String, Stirng>
   */
  private Map<String, String> requestHeader() throws Exception {
    if (!br.ready()) return new HashMap<>();
    String line;

    Map<String, String> requestHeader = new HashMap<>();
    while (br.ready()) {
      if ("".equals(line = br.readLine())) break;
      requestHeader.putAll(HttpRequestUtils.getKeyValueMap(line, ": "));
    }

    return requestHeader;
  }

  /**
   * Feat : requestBody 를 만듭니다.
   * Desc : contentLength 만큼 만듭니다.
   * Return : String
   */
  private String requestBody() throws Exception {
    return (br.ready() && header.containsKey("Content-Length"))
        ? IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")))
        : "";
  }

  public String getMethod() {
    return this.startLine.get("method");
  }

  public String getPath() {
    return this.startLine.get("requestUrl");
  }

  public Map<String, String> getHeader() {
    return this.header;
  }

  public String getBody() {
    return this.body;
  }

  public Map<String, String> getStartLine() {
    return this.startLine;
  }

}
