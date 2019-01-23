package codesquad.model.responses;

import codesquad.webserver.ViewResolver;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static codesquad.webserver.WebServer.ROOT_STATIC_PATH;
import static org.slf4j.LoggerFactory.getLogger;

public class HttpResponse {
    private static final Logger log = getLogger(HttpResponse.class);

    private List<String> accpets;

    private ResponseCode responseCode;

    private String accessPath;

    private Map<String, String> cookie;

    private byte[] body;

    public HttpResponse(List<String> accpets, ResponseCode responseCode, String accessPath, Map<String, String> cookie) {
        this.accpets = accpets;
        this.responseCode = responseCode;
        this.accessPath = accessPath;
        this.cookie = cookie;
    }

    public ResponseTemplate chooseTemplate(Map<ResponseCode, ResponseTemplate> responses) {
        return responses.get(responseCode);
    }

    public void writeBody() throws IOException {
        Path filePath = Paths.get(ROOT_STATIC_PATH + this.accessPath);
        if (filePath.getFileName().toString().equals("list.html")) {
            this.body = ViewResolver.renewUserList();  // todo 하드코딩 없애야함
            return;
        }
        this.body = Files.readAllBytes(filePath);
    }

    public String writeContentType() {
        if(accpets.contains("text/css")) return "Content-Type: text/css;charset=utf-8\r\n";
        return "Content-Type: text/html;charset=utf-8\r\n";
    }

    public String writeCookie() {
        if(this.cookie.isEmpty()) return "";
        StringBuilder cookieBuilder = new StringBuilder();
        for (String key : cookie.keySet()) {
            cookieBuilder.append(key).append("=").append(cookie.get(key)).append(";");
        }
        StringBuilder templateBuilder = new StringBuilder("Set-Cookie: ").append(cookieBuilder)
                .append(" Path=/").append("\r\n");
        return templateBuilder.toString();
    }

    public String writeContentLength() {
        return new StringBuilder("Content-Length: ").append(body.length).append("\r\n").toString();
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

    public String writeLocation() {
        return new StringBuilder("Location: ").append(this.accessPath).append("\r\n").toString();
    }
}
