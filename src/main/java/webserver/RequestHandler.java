package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            log.debug("request line: {} ", line);

            if (line == null) {
                return;
            }

            String[] tokens = line.split(" ");

            String url = tokens[1];
            userSignUpWithPostMethod(url, br);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private User userSignUpWithGetMethod(String url) {
        final String questionMark = "?";
        if (url.startsWith("/user/create")) {
            int index = url.indexOf(questionMark);
            String queryString = url.substring(index + 1);
            log.debug("queryString: {}", queryString);
            Map<String, String> parameterMap = HttpRequestUtils.parseQueryString(queryString);
            User user = User.ofMap(parameterMap);
            log.debug("user: {}", user);
            return user;
        }
        return null;
    }

    private User userSignUpWithPostMethod(String url, BufferedReader br) throws IOException {
        if (url.startsWith("/user/create")) {
            int contentLength = contentLengthParser(br);
            String body = IOUtils.readData(br, contentLength);
            log.debug("body: {}", body);
            Map<String, String> parameterMap = HttpRequestUtils.parseQueryString(body);
            log.debug("parameterMap: {}", parameterMap);
            User user = User.ofMap(parameterMap);
            log.debug("user: {}", user);
            return user;
        }
        return null;
    }

    private int contentLengthParser(BufferedReader br) throws IOException {
        int contentLength = 0;
        String line = " ";
        //TODO br.readLine()의 작동방식 살펴보기
        while (true) {
            line = br.readLine();
            log.debug("this is line: {}", line);
            if (line.contains("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
                break;
            }
            if (line.equals("")) {
                break;
            }
        }
        return contentLength;
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
