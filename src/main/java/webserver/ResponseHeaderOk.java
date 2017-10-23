package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;

public class ResponseHeaderOk extends ResponseHeader {

    private ResponseCodes code = ResponseCodes.OK_200;

    public String generateHttpResponseHeader(int lengthOfBodyContent) {

        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 200 OK \r\n");
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

}
