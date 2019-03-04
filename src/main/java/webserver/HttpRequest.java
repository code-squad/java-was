package webserver;
import db.DataBase;
import model.HttpMethod;
import model.User;
import org.slf4j.Logger;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpRequest {
    private static final Logger logger = getLogger(HttpRequest.class);

    private static final String URL_CREATE = "/user/create";
    private static final String URL_LOGIN = "/user/login";

    private HttpMethod method;
    private String path;
    private boolean login = false;
    private Map<String, String> httpRequest = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        method = HttpRequestUtils.getHttpMethod(line);
        path = HttpRequestUtils.getPath(line);

        if (path.contains("?")) {
            String[] paths = path.split("\\?");
            path = paths[0];
            Map<String, String> map = HttpRequestUtils.parseQueryString(paths[1]);
            httpRequest.putAll(map);
        }

        while (!line.equals(""))  {
            line = br.readLine();

            if (line.trim().equals("")) {
                break;
            }
            logger.debug("### {}", line);
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            httpRequest.put(pair.getKey(), pair.getValue());
        }

        if (httpRequest.get("Content-Length") != (null)) {
            int contentLength = Integer.parseInt(httpRequest.get("Content-Length"));
            String body = IOUtils.readData(br, contentLength);

            Map<String, String> map = HttpRequestUtils.parseQueryString(body);
            httpRequest.putAll(map);

            if (this.getPath().equals(URL_CREATE)) {
                User user = new User();
                user.setUserId(map.get("userId"));
                user.setName(map.get("name"));
                user.setPassword(map.get("password"));
                user.setEmail(map.get("email"));

                DataBase.addUser(user);


            } else if (this.getPath().equals(URL_LOGIN)) {
                String userId = map.get("userId");
                String password = map.get("password");

                User user = DataBase.findUserById(userId);

                if (user.matchPassword(password)) {
                    login = true;
                }
            }
        }
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public String getHeader(String header) {
        return httpRequest.get(header);
    }

    public String getParameter(String header) {
        return httpRequest.get(header);
    }

    public boolean isLogin() {
        return login;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
