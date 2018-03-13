package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private DataOutputStream dos;
    public Map<String, String> header = new HashMap<>();

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void forward(String URI, String contentType, byte[] body) throws IOException {
        // path 에 해당하는 파일을 읽어온다.
        response200Header(body.length, contentType);
        responseBody(body);
    }

    public void sendRedirect(String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public byte[] readFileToByte(String URI) throws IOException {
        return Files.readAllBytes(new File("./webapp" + URI).toPath());
    }

    public byte[] createDynamicHTML(String staticHtmlFileURI, List<User> users) throws IOException {
        BufferedReader br = new BufferedReader(
                new FileReader(new File(staticHtmlFileURI)));
        StringBuilder sb = writeDynamicHTML(users, br);
        String s = sb.toString();
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private StringBuilder writeDynamicHTML(List<User> users, BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        writeStaticPart(br, sb);
        writeDynamic(sb, users);
        writeStaticPart(br, sb);
        return sb;
    }

    public void writeDynamic(StringBuilder sb, List<User> users) {
        for(User user : users){
            sb.append("<tr> \r\n");
            sb.append("<th scope=\"row\">" + (users.indexOf(user)+1) + "</th> <td>" + user.getUserId() +"</td> <td>" + user.getName() +"</td> <td>"+ user.getEmail() +"</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            sb.append("</tr> \r\n");
        }
    }

    private void writeStaticPart(BufferedReader br, StringBuilder sb) throws IOException {
        String line = br.readLine();
        if(line == null || line.equals("")) return;
        sb.append(line + "\r\n");
        writeStaticPart(br, sb);
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseUserSignUp(HttpRequest httpRequest, String location) {
        String requestBody = httpRequest.getRequestBody();
        log.debug("requestBody : {}", requestBody);
        createUser(httpRequest, requestBody);
        sendRedirect(location);
        return;
    }

    public void responseUserSignIn(String userId) {
        DataBase db = new DataBase();
        if(db.findUserById(userId) != null){// 로그인 성공시
            responseWithCookie(true, "/index.html");
            return;
        }
        responseWithCookie(false, "/user/login_failed.html");
        return;
    }
    public void createUser(HttpRequest httpRequest, String queryString) {
        Map<String, String> parameters = httpRequest.getRequestParameter(queryString);
        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        DataBase db = new DataBase();
        db.addUser(user);
    }

    public void response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseWithCookie(boolean logined, String location){
        try {
            sendRedirect(location);
            dos.writeBytes("Set-Cookie:" + "logined=" + logined + ";" + "Path=/");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String header, String value) {
        try {
            dos.writeBytes(header + ": " + value + "\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

}
