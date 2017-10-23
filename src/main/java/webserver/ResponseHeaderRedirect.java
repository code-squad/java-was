package webserver;

import org.slf4j.Logger;

public class ResponseHeaderRedirect extends ResponseHeader {

    public String generateHttpResponseHeader(Logger log, String target) {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Location: " + target);

        return sb.toString();
    }

}
