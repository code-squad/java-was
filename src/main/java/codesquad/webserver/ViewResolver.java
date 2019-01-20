package codesquad.webserver;

import codesquad.db.UserRepository;
import codesquad.model.User;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

public class ViewResolver {
    private static final Logger log = getLogger(ViewResolver.class);

    // temp
    public static byte[] renewUserList() throws IOException {
        Path path = Paths.get("./webapp/user/list.html");
        String contents = readFile(path, Charset.forName("UTF-8"));
        contents = changeContent(contents);
        return contents.getBytes();
    }

    private static String readFile(Path path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }

    private static String changeContent(String contents) {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (User user : UserRepository.findAll()) {
            sb.append(" <tr> <th scope=\"row\"> " + index + "</th> <td>" + user.getUserId() + "</td> <td>" + user.getName() +"</td> <td>" + user.getEmail() + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td> </tr> ");
            index++;
        }
        return contents.replace("{{ users }}", sb.toString());
    }
}
