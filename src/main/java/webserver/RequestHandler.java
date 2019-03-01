package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import db.DataBase;
import model.HttpMethod;
import model.User;
import org.omg.CORBA.DATA_CONVERSION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.RequestLineUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String baseUrl = "./webapp";
    private static final String URL_CREATE = "/user/create";
    private static final String URL_LOGIN = "/user/login";
    private static final String URL_USER_LIST = "/user/list";

    private Socket connection;
    private HttpRequest request;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            log.debug("request line : {}", line);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "".getBytes();
            String header = null;

            request = new HttpRequest(line);
            log.debug("########## run : {}",  request);

            if (request.getHttpMethod().equals(HttpMethod.GET)) {
                body = requestMethodGet(line, br);

                if (request.getPath().endsWith("html")) {
                    header = response200Header("text/html", body.length);
                } else {
                    header = response200Header("text/css", body.length);
                }

            } else if (request.getHttpMethod().equals(HttpMethod.POST)) {
                header = requestMethodPost(line, br);
            }

//            byte[] body = "Hello World".getBytes();

            try {
                dos.writeBytes(header);
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String response200Header(String contentType, int lengthOfBodyContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK \r\n");
        sb.append("Content-Type: " + contentType + ";charset=utf-8\r\n");
        sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    private String response302Header(String url, boolean isLogin) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Location : " + url + "\r\n");
        sb.append("Set-Cookie: logined=" + isLogin + "; Path=/\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] requestMethodGet(String line, BufferedReader br) throws IOException {
        String url = RequestLineUtils.getUrl(line);
        while(!line.equals("")) {
            line = br.readLine();



            request.getHeader(line);
            log.debug("########## run : {}",  request);






            log.debug("header : {}", line);

            if (url.equals(URL_USER_LIST)) {
                if (line.contains("Cookie")) {
                    String[] str = line.split(" ");
                    if (str[1].equals("logined=false")) {
                        url = "/user/login.html";
                    } else {
                        StringBuilder sb = new StringBuilder();
                        Collection<User> list = DataBase.findAll();
                        for (User user : list) {
                            sb.append(user.getUserId());
                            sb.append(user.getName());
                            sb.append(user.getEmail());
                        }
                    }
                }
            }
        }
        return Files.readAllBytes(new File(baseUrl + url).toPath());
    }

    private String requestMethodPost(String line, BufferedReader br) throws IOException {
        boolean isPost = false;
        int length = 0;
        String header = null;

        String url = RequestLineUtils.getUrl(line);

        while(!line.equals("")) {
            line = br.readLine();
            log.debug("header : {}", line);


            request.getHeader(line);
            log.debug("########## run : {}",  request);

            isPost = line.contains("Content-Length");

            if (isPost) {
                length = Integer.parseInt(line.substring(16));
            }
        }

        if (url.equals(URL_CREATE)) {
            String data = IOUtils.readData(br, length);
            Map<String, String> map = HttpRequestUtils.parseQueryString(data);
            User user = new User();
            user.setUserId(map.get("userId"));
            user.setName(map.get("name"));
            user.setPassword(map.get("password"));
            user.setEmail(map.get("email"));

            DataBase.addUser(user);

            log.debug("log : {}" , user.toString());

            header = response302Header("/index.html", false);

        } else if (url.equals(URL_LOGIN)) {
            String data = IOUtils.readData(br, length);
            Map<String, String> map = HttpRequestUtils.parseQueryString(data);
            String userId = map.get("userId");
            String password = map.get("password");

            User user = DataBase.findUserById(userId);
            log.debug("저장된 user : {}", user);

            if (user.matchPassword(password)) {
                header =  response302Header("/index.html", true);
            } else {
                header =  response302Header("/user/login_failed.html", false);
            }
        }

        log.debug("===== header : {} =====\n", header);

        return header;
    }
}
