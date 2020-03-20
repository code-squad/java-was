package model;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpTemplate {

  public HttpRequest(BufferedReader br) throws Exception {
    this.br = br;
    this.startLine = requestLine();
//      Map<String, HttpRequestUtils.Pair> requestLine = requestLine(br);
//      Map<String, HttpRequestUtils.Pair> requestHeader = requestHeader(br);
//      String requestBody = "";
//      if (requestHeader.containsKey("Content-Length")) {
//        requestBody = requestBody(br, Integer.parseInt(requestHeader.get("Content-Length").getValue()));
//      }
  }

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

  public String getMethod() {
    return this.startLine.get("method");
  }

  public String getPath() {
    return this.startLine.get("requestUrl");
  }

}
