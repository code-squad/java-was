package model;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseEntity {

    private static final Logger logger = getLogger(ResponseEntity.class);

    private byte[] body;
    private Map<String, String> header;
    private static Map<String, String> statusMapper = new HashMap<>();
    private static Map<String, Consumer<DataOutputStream>> statusProcessor = new HashMap<>();

    static {
        statusMapper.put("200", "OK");
        statusMapper.put("302", "FOUND");
    }

    public ResponseEntity(byte[] body) {
        this.header = new HashMap<>();
        this.body = body;

        initStatusMapper();
    }

    public void initStatusMapper()  {
        statusProcessor.put("200", (dos) -> response200Header(dos));
        statusProcessor.put("302", (dos) -> response302Header(dos));
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ResponseEntity addHeader(String key, String value) {
        header.put(key, value);
        return this;
    }

    /*
       @param
       @return response에 대한 공통적인 속성을 작성한 템플
    */
    public ResponseEntity responseHeader(DataOutputStream dos) throws IOException {

        String statusCode = obtainStatusCode();
        dos.writeBytes(String.format("HTTP/1.1 %s %s\r\n", statusCode, obtainStatus(statusCode)));
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes(String.format("Content-Length: %d\r\n", this.body.length));

        /* status에 따라 response 구성이 조금씩 다르기 때문에 다른 부분은 람다로 처리 */
        statusProcessor.get(statusCode).accept(dos);

        dos.writeBytes("\r\n");

        return this;
    }

    public String obtainStatusCode() {
        return this.header.get("status");
    }

    public String obtainStatus(String statusCode) {
        return statusMapper.get(statusCode);
    }

    private void response302Header(DataOutputStream dos) {
        try {
            logger.debug("Location : {}", header.get("Location"));
            dos.writeBytes(String.format("Location: %s", header.get("Location")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos) {
        if(header.containsKey("Set-Cookie")) {
            try {
                logger.debug("Cookie 적용 완료!");
                dos.writeBytes(String.format("Set-Cookie: %s", header.get("Set-Cookie")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ResponseEntity responseBody(DataOutputStream dos) {
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
        return "ResponseEntity{" +
                "header=" + header +
                '}';
    }
}
