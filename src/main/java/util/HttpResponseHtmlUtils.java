package util;

import model.User;

import java.util.Collection;

public class HttpResponseHtmlUtils {
    public static String generate(Collection<User> users) {
        StringBuilder sb = new StringBuilder()
                .append("<html>").append(System.lineSeparator())
                .append("<body>").append(System.lineSeparator());
        for (User user : users) {
            sb.append("<p>")
                    .append(user.toString())
                    .append("</p>")
                    .append(System.lineSeparator());
        }
        sb.append("</body>").append(System.lineSeparator())
                .append("</html>");
        return sb.toString();
    }
}
