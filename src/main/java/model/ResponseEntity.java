package model;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseEntity {

    private static final Logger logger = getLogger(ResponseEntity.class);

    private int statusCode;
    private byte[] body;
    private String location;
    private Map<Integer, Consumer<DataOutputStream>> statusMapper = new HashMap<>();

    public ResponseEntity(int statusCode, String location, byte[] body) {
        this.statusCode = statusCode;
        this.location = location;
        this.body = body;

        initStatusMapper();
    }

    public void initStatusMapper()  {
        statusMapper.put(200, (dos) -> response300Header(dos));
        statusMapper.put(302, (dos) -> response302Header(dos));
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /*
       @param
       @return response에 대한 공통적인 속성을 작성한 템플
    */
    public ResponseEntity responseHeader(DataOutputStream dos) {
        try {
            /* 여기서 Socket Close 발생! why..? -> socket 의 input/output stream이 닫히면 socket도 같이 닫히는 현상 발생! */
            dos.writeBytes(String.format("HTTP/1.1 %d %s\r\n", this.statusCode, statusMapper.get(this.statusCode)));
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes(String.format("Content-Length: %d\r\n", this.body.length));

            /* status에 따라 response 구성이 조금씩 다르기 때문에 다른 부분은 람다로 처리 */
            statusMapper.get(this.statusCode).accept(dos);

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return this;
    }

    private void response302Header(DataOutputStream dos) {
        try {
            logger.debug("Create 302 Header");
            dos.writeBytes(String.format("Location: %s", location));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response300Header(DataOutputStream dos) {
        logger.debug("Create 300 Header");
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

    public boolean hasLocation() {
        return location != null;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "statusCode=" + statusCode +
                ", location='" + location + '\'' +
                ", statusMapper=" + statusMapper +
                '}';
    }
}
