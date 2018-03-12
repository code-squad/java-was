package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void readFile(String URI, String contentType) throws IOException {
        // path 에 해당하는 파일을 읽어온다.
        byte[] body = readFileToByte(URI);
        response200Header(body.length, contentType);
        responseBody(body);
    }

    private byte[] readFileToByte(String URI) throws IOException {
        return Files.readAllBytes(new File("./webapp" + URI).toPath());
    }

    public void createDynamicHTML(String staticHtmlFileURI, List<User> users, String contentType) throws IOException {
        // read static part in file first
        BufferedReader br = new BufferedReader(
                new FileReader(new File(staticHtmlFileURI)));
        StringBuilder sb = writeDynamicHTML(users, br);
        String s = sb.toString();
        byte[] body = s.getBytes(StandardCharsets.UTF_8);
        response200Header(body.length, contentType);
        responseBody(body);
    }

    private StringBuilder writeDynamicHTML(List<User> users, BufferedReader br) throws IOException {
        // write static part
        StringBuilder sb = new StringBuilder();
        writeStaticPart(br, sb);
        // write dynamic part
        writeDynamic(sb, users);
        // write static part
        writeStaticPart(br, sb);
        return sb;
    }

    private void writeStaticPart(BufferedReader br, StringBuilder sb) throws IOException {
        String line = br.readLine();
        sb.append(line + "\r\n");
        while(!line.equals("")) {
            line = br.readLine();
            if(line == null) break;
            sb.append(line + "\r\n");
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void writeDynamic(StringBuilder sb, List<User> users) {
        for(User user : users){
            sb.append("<tr> \r\n");
            sb.append("<th scope=\"row\">" + (users.indexOf(user)+1) + "</th> <td>" + user.getUserId() +"</td> <td>" + user.getName() +"</td> <td>"+ user.getEmail() +"</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            sb.append("</tr> \r\n");
        }
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

    public void responseUserSignUp(HttpRequest httpRequest, String location) {
        String requestBody = httpRequest.getRequestBody();
        log.debug("requestBody : {}", requestBody);
        createUser(httpRequest, requestBody);
        response302Header(location);
        return;
    }

    public void responseUserSignIn(HttpRequest httpRequest) {
        String requestBody = httpRequest.getRequestBody();
        log.debug("requestBody : {}", requestBody);
        Map<String, String> parameters = httpRequest.getRequestParameter(requestBody);
        log.debug("id, password: {}", parameters.toString());
        String id = parameters.get("userId");
        DataBase db = new DataBase();
        boolean status = false;
        if(db.findUserById(id) != null){// 로그인 성공시
            status = true;
            String location = "/index.html";
            response302HeaderWithCookieLoginSuccess(status, location);
            return;
        }
        String location = "/user/login_failed.html";
        response302HeaderWithCookieLoginFail(status, location);
        return;
    }

    public void createUser(HttpRequest httpRequest, String queryString) {
        Map<String, String> parameters = httpRequest.getRequestParameter(queryString);
        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        DataBase db = new DataBase();
        db.addUser(user);
    }

    public void response302Header(String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderWithCookieLoginSuccess(boolean status, String location){
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
            dos.writeBytes("Location: " + location + " \r\n");
            dos.writeBytes("Set-Cookie:" + "logined=" + status + ";" + "Path=/");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderWithCookieLoginFail(boolean status, String location){
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Set-Cookie:" + "logined=" + status + ";" + "Path=/");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderWithCookieLoginRequired(){
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
            dos.writeBytes("Location: /user/login.html \r\n");
//            dos.writeBytes("Set-Cookie:" + "logined=" + status + ";" + "Path=/");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }




}
