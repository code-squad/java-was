package model;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpResponse {

    private static final Logger logger = getLogger(HttpResponse.class);

    private byte[] body;
    private HttpHeader httpHeader;
    private static Map<String, String> statusMapper = new HashMap<>();
    private static Map<String, Consumer<DataOutputStream>> statusProcessor = new HashMap<>();

    static {
        statusMapper.put("200", "OK");
        statusMapper.put("302", "FOUND");
    }

    public HttpResponse(byte[] body) {
        this.httpHeader = new HttpHeader();
        this.body = body;

        initStatusMapper();
    }

    public void initStatusMapper() {
        statusProcessor.put("200", (dos) -> response200Header(dos));
        statusProcessor.put("302", (dos) -> response302Header(dos));
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public HttpResponse addHeader(String key, String value) {
        httpHeader.addHeader(key, value);
        return this;
    }

    /*
       @param
       @return response에 대한 공통적인 속성을 작성한 템플
    */
    public HttpResponse responseHeader(DataOutputStream dos, boolean isResource) throws IOException {

        String statusCode = httpHeader.obtainHeader("status");
        dos.writeBytes(String.format("HTTP/1.1 %s %s\r\n", statusCode, obtainStatus(statusCode)));
        String contentType = "Content-Type: text/html;charset=utf-8\r\n";
        if(isResource) {
            contentType = "Content-Type: text/css\r\n";
        }
        dos.writeBytes(contentType);

        /* status에 따라 response 구성이 조금씩 다르기 때문에 다른 부분은 람다로 처리 */
        statusProcessor.get(statusCode).accept(dos);
        dos.writeBytes(String.format("Set-Cookie: %s\r\n", httpHeader.obtainHeader("Set-Cookie")));
        dos.writeBytes("\r\n");

        return this;
    }

    public String obtainStatus(String statusCode) {
        return statusMapper.get(statusCode);
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes(String.format("Location: %s\r\n", httpHeader.obtainHeader("Location")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos) {
        try {
            dos.writeBytes(String.format("Content-Length: %d\r\n", this.body.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpResponse responseBody(DataOutputStream dos) {
        try {
            dos.write(this.body, 0, this.body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.getStackTrace();
        }
        return this;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "body=" + Arrays.toString(body) +
                ", httpHeader=" + httpHeader +
                '}';
    }
}
