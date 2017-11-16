package webserver;

public class ResponseHeaderOk extends ResponseHeader {


    public String generateHttpResponseHeader(int lengthOfBodyContent) {

        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 200 OK \r\n");
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

}
