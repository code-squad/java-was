package util;

import model.User;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class RequestLineUtils {
    private static final Logger logger = getLogger(RequestLineUtils.class);
    private static final String URL_USER_CREATE = "/user/create";



    public static String getUrl(String input) {
        String[] strings = input.split(" ");

        //GET /index.html HTTP/1.1
        if (strings[1].endsWith(".html")) {
            return strings[1];
        }


        /*
        //GET /user/create?userId=11&password=1122&name=33&email=1%401 HTTP/1.1
        if (strings[1].startsWith(URL_USER_CREATE)) {
            String[] queryString = strings[1].split("\\?");
            Map<String, String> map = HttpRequestUtils.parseQueryString(queryString[1]);

            User user = new User();
            user.setUserId(map.get("userId"));
            user.setName(map.get("name"));
            user.setPassword(map.get("password"));
            user.setEmail(map.get("email"));

            logger.debug("## getUrl : {}",  user.toString());;
        }
        */
        return strings[1];
    }


    public static String getHttpMethod(String requestLine) {
        String[] requestLines = requestLine.split(" ");
        return requestLines[0];
    }
}
