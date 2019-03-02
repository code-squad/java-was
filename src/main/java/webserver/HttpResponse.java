package webserver;

public class HttpResponse {


    //public String response200


//
//
//    private String response200Header(String contentType, int lengthOfBodyContent) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("HTTP/1.1 200 OK \r\n");
//        sb.append("Content-Type: " + contentType + ";charset=utf-8\r\n");
//        sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
//        sb.append("\r\n");
//        return sb.toString();
//    }

    public void addHeader(String statusCode, String location) {}
    public void forward(String string) {}
    public void forwardBody(String string) {}
    public void response200Header(int i) {}
    public void responseBody(byte[] b) {}
    public void sendRedirect(String string) {}
    public void processHeader() {}

}

