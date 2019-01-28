package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static String generateUsersBody(List<User> users) {
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
        return sb.toString();
    }

}
