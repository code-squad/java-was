package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String[] firstLines = br.readLine().split(" ");
            String method = firstLines[0];
            String requestUrl = firstLines[1];
            HashMap<String, String> headerMap = getHeaderObject(br);
            log.debug("it's method : {}", method);

            if (method.equals("POST")) responsePost(br, out, headerMap, requestUrl);

            if (method.equals("GET")) responseGet(requestUrl, out, headerMap);


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseGet(String query, OutputStream out, HashMap<String, String> headers) throws IOException {
        String url = (query.equals("/")) ? "/index.html" : query;

        byte[] body = null;

        if (query.equals("/user/list") || query.equals("/user/list.html") ) {
            if (isLogined(headers)) {
                body = createHtml(query);
            }
        }

        log.debug("request url : {}", url);
        DataOutputStream dos = new DataOutputStream(out);
        if (body == null) body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length, headers);
        responseBody(dos, body);
    }

    private boolean isLogined(HashMap<String, String> headers){
        boolean logined = false;
        if (headers.containsKey("Cookie")){
            log.debug("it's get islogined: {}", headers.get("Cookie"));
            logined = headers.get("Cookie").contains("logined=true");
        }

        return logined;
    }

    private byte[] createHtml(String url) throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get("./webapp/user/list.html"));
        lines.forEach(s -> builder.append(s.replace("{{user}}", createUserList(DataBase.findAll()))));
        return builder.toString().getBytes();
    }

    private String createUserList(Collection<User> users) {
        if (users.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        users.forEach(user -> {
            builder.append(
                    String.format("<tr>\n" +
                            "<th scope=\"row\">1</th><td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                            "</tr>", user.getUserId(), user.getName(), user.getEmail()));
        });

        return builder.toString();
    }

    private void responsePost(BufferedReader br, OutputStream out, HashMap<String, String> headers, String requestUrl) throws IOException {
        Map<String, String> queryMap = HttpRequestUtils.parseQueryString(IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))));
        DataOutputStream dos = new DataOutputStream(out);
        String redirectUrl = "/index.html";
        boolean logined = isLogined(headers);
        switch (requestUrl) {
            case "/user/create":
                createUser(queryMap);
                break;
            case "/user/login":
                logined = login(queryMap.get("userId"), queryMap.get("password"));
                redirectUrl = (logined) ? "/index.html" : "/user/login_failed.html";
        }

        response302Header(dos, redirectUrl, logined);
        responseBody(dos, new byte[0]);
    }

    private void createUser(Map<String, String> parameter) {
        User newUser = new User(parameter.get("userId"), parameter.get("password"), parameter.get("name"), parameter.get("email"));
        DataBase.addUser(newUser);
        log.debug("created user : {}", newUser);
    }

    private boolean login(String userId, String password) {
        if (!DataBase.findUserById(userId).getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    private HashMap<String, String> getHeaderObject(BufferedReader br) throws IOException {
        String line = br.readLine();
        HashMap<String, String> pairs = new HashMap<>();
        while (!line.isEmpty()) {
            line = br.readLine();
            if (line.isEmpty()) {
                break;
            }
            log.debug("line: {}", line);

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            if (pair != null) {
                pairs.put(pair.getKey(), pair.getValue());
            }
        }
        return pairs;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, HashMap<String, String> headers) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", headers.get("Accept").split(",")[0]));
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes(String.format("Set-Cookie: logined=%s; Path=/ \r\n", (isLogined(headers)) ? "true" : "false"));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl, boolean logined) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes(String.format("Set-Cookie: logined=%s; Path=/ \r\n", (logined) ? "true" : "false"));
            dos.writeBytes(String.format("Location: %s \r\n", redirectUrl));
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
