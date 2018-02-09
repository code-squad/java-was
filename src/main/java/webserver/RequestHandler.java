package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
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

            String line = br.readLine();

            if (line == null)
                return;

            String[] url = line.split(" ");
            String HttpMethod = url[0];
            String path = url[1];

            Map<String, String> header = new HashMap<>();

            while (!line.equals("")) {
                System.out.println(line);
                line = br.readLine();

                if (!line.equals("")) {
                    String[] keyValue = line.split(": ");
                    header.put(keyValue[0], keyValue[1]);
                }
            }

            switch (HttpMethod) {
                case "GET":
                    get(out, path);
                    break;
                case "POST":
                    post(br, out, header, path);
                    break;
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void get(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void post(BufferedReader br, OutputStream out, Map<String, String> header, String path) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        int contentLength = Integer.parseInt(header.get("Content-Length"));
        String requestBody = IOUtils.readData(br, contentLength);
        switch (path) {
            case "/user/create":
                saveUser(requestBody);
                response302Header(dos);
                break;
            case "/user/login":
                response302Header(dos);
                login(requestBody, dos);
                break;
        }
    }

    private void saveUser(String body) {
        Map<String, String> map = HttpRequestUtils.parseQueryString(body);
        User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        DataBase.addUser(user);
    }

    private DataOutputStream login(String body, DataOutputStream dos) throws IOException {
        Map<String, String> map = HttpRequestUtils.parseQueryString(body);
        User user = DataBase.findUserById(map.get("userId"));
        if (user == null || !user.getPassword().equals(map.get("password"))) {
            dos.writeBytes("Location: /user/login_failed.html\r\n");
            dos.writeBytes("Set-Cookie: logined=false; Path=/ \r\n");
            dos.writeBytes("\r\n");
            return dos;
        }
        dos.writeBytes("Location: /index.html\r\n");
        dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
        dos.writeBytes("\r\n");
        return dos;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
