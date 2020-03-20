package util;


import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static final String PAGE_LOGIN_FAILED = "/user/login_failed.html";


    public static boolean isFileExist(String path) {
        return Files.exists(Paths.get(new File("./webapp") + path));
    }

    public static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(new File("./webapp" + path).toPath());
    }

    public static byte[] notExistPage() {
        return "요청하신 페이지가 없습니다".getBytes();
    }

    public static String getUserListHTML(byte[] body, List<User> users) {
        StringBuilder list = new StringBuilder(new String(body));
        String target = "<table class=\"table table-hover\">";
        int index = list.indexOf(target);
        list.insert(index + target.length(), createUserListHTML(users));
        return list.toString();
    }

    private static String createUserListHTML(List<User> users) {
        StringBuilder sb = new StringBuilder();

        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");

        for (int i = 0; i < users.size(); i++) {
            sb.append("<tr>");
            sb.append("<th scope=\"row\">").append(i + 1).append("</th>");
            sb.append("<td>").append(users.get(i).getUserId()).append("</td>");
            sb.append("<td>").append(users.get(i).getName()).append("</td>");
            sb.append("<td>").append(users.get(i).getEmail()).append("</td>");
            sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        return sb.toString();
    }
}
