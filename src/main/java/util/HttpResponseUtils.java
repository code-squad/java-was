package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class HttpResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static byte[] generateBody(String path) throws IOException {
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }

    public static byte[] generateUsersBody(List<User> users) {
        StringBuilder sb = new StringBuilder("<html>\n")
                .append("<body>\n")
                .append("<h1>users</h1>\n")
                .append("<ul>\n");
        for (User user : users) {
            sb.append("<li>");
            sb.append("ID : ").append(user.getUserId()).append(", NAME : ").append(user.getName());
            sb.append("</li>\n");
        }
        sb.append("</ul>\n").append("</body>\n").append("</html>\n");
        return sb.toString().getBytes();
    }

    public static void response302Header(HttpResponse response, String location) {
        try {
            response.writeHeader("HTTP/1.1 302 Found \r\n");
            response.writeHeader("Location: " + location + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void response200Header(HttpResponse response, int lengthOfBodyContent, String contentType) {
        try {
            response.writeHeader("HTTP/1.1 200 OK \r\n");
            response.writeHeader("Content-Type: " + contentType + "\r\n");
            response.writeHeader("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.writeHeader("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void responseBody(HttpResponse response, byte[] body) {
        try {
            response.writeBody(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void responseSend(HttpResponse response) {
        try {
            response.send();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void responseCookieHeader(HttpResponse response, boolean success) {
        try {
            response.writeHeader("Set-Cookie: logined=" + success + "; Path=/\r\n");
            response.writeHeader("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
