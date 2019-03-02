package webserver;

public class ResponseBuilder {
    private StringBuilder sb;

    public ResponseBuilder() {
        this.sb = new StringBuilder();
        this.sb.append("HTTP/1.1");
    }

    public ResponseBuilder addStatusCode(String statusCode) {
        this.sb.append(statusCode);
        return this;
    }

    public static ResponseBuilder encodeForm() {
        return new ResponseBuilder();
    }
}
//    StringBuilder sb = new StringBuilder();
//        sb.append("HTTP/1.1 200 OK \r\n");
//                sb.append("Content-Type: " + contentType + ";charset=utf-8\r\n");
//                sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
//                sb.append("\r\n");
//                return sb.toString();
