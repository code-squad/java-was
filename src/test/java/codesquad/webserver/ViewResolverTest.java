package codesquad.webserver;

import codesquad.db.UserRepository;
import codesquad.model.User;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class ViewResolverTest {
    private static final Logger log = getLogger(ViewResolverTest.class);

    @Test
    public void readFile() throws IOException {
        Path path = Paths.get("./webapp/user/list.html");
        byte[] body = Files.readAllBytes(path);
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String allLine : allLines) {
            if(allLine.contains("{{ users }}")) {
                StringBuilder sb = new StringBuilder();
                int index = 1;
                for (User user : UserRepository.findAll()) {
                    sb.append(" <tr> <th scope=\"row\"> " + index + "</th> <td>" + user.getUserId() + "</td> <td>" + user.getName() +"</td> <td>" + user.getEmail() + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td> </tr> ");
                }
            }
        }
    }

    @Test
    public void replaceTest() {
        String test = "{{ users }} hi I'm brad";
//        test.replace("{{ users }}", "Good!");
        assertThat(test.replace("{{ users }}", "Good!")).isEqualTo("Good! hi I'm brad");
    }
}