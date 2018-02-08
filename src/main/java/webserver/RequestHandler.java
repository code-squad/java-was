package webserver;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
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
            String line = br.readLine();
            log.debug("BufferedReader : {}", br);
            String[] firstLines = line.split(" ");

            if (firstLines[0].equals("POST")) responsePost(br, out);

            if (firstLines[0].equals("GET")) responseGet(firstLines[1], out);


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responsePost(BufferedReader br, OutputStream out) throws IOException {
        String query = IOUtils.readData(br, Integer.parseInt(getHeaderObject(br).get("Content-Length")));

        log.debug("query : {}", query);

        Map<String, String> queryMap = HttpRequestUtils.parseQueryString(query);
        createUser(queryMap);

        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);

    }

    private void responseGet(String query, OutputStream out) throws IOException {
        //디폴트 주소를 index.html로 변경하기 위함
        String url = (query.equals("/")) ? "/index.html" : query;

        //GET Method 내 파라미터를 처리하는 로직
        if (url.contains("?")) {
            Map<String, String> queryMap = HttpRequestUtils.parseQueryString(url.split("\\?")[1]);
            createUser(queryMap);
            url = "/index.html";
        }
        log.debug("request url : {}", url);
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    public void createUser(Map<String, String> parameter) {
        User newUser = new User(parameter.get("userId"), parameter.get("password"), parameter.get("name"), parameter.get("email"));
        DataBase.addUser(newUser);
        log.debug("created user : {}", newUser);

    }

    public HashMap<String, String> getHeaderObject(BufferedReader br) throws IOException{
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
