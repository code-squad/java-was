package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;

public class ResponseHeaderRedirect extends ResponseHeader {

    private ResponseCodes code = ResponseCodes.REDIRECT_302;

    public String generateHttpResponseHeader(Logger log, String target) {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Location: " + target);

        return sb.toString();
    }

}
