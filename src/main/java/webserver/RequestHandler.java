package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HeaderPathUtils;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    RequestHandler(Socket connectionSocket) {
        connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line = "";
            String path = "";
            boolean isPost = false;
            boolean isLogin = false;
            boolean isLoginSucess = false;

            try {
                line = br.readLine();
                path = HeaderPathUtils.extractPath(line);

                log.debug("path : {}", path);

                Map<String, String> headers = new HashMap<>();

                if (HeaderPathUtils.checkPost(line)) {
                    while (!"".equals(line)) {
                        line = br.readLine();

                        if (line == null || line.equals("")) {
                            break;
                        }

                        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                        headers.put(pair.getKey(), pair.getValue());
                    }

                    isPost = true;

//                    br.readLine();

                    String data = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                    log.debug("data : {}", data);

                    if (path.startsWith("/user/create")) {
                        User user = new User(data);
                        log.debug("user : {}", user);

                        DataBase.addUser(user);
                        User foundUser = DataBase.findUserById(user.getUserId());
                        log.debug("found user : {}", foundUser);

                        path = "/user/list.html";
                    } else if (path.startsWith("/user/login")) {
                        isLogin = true;

                        Map<String, String> parsed = HttpRequestUtils.parseQueryString(data);
                        String userId = parsed.get("userId");
                        String password = parsed.get("password");
                        User foundUser = DataBase.findUserById(userId);

                        if (foundUser != null && foundUser.matchPassword(password)) {
                            isLoginSucess = true;
                            path = "/index.html";
                        } else {
                            path = "/user/login_failed.html";
                        }
                    }

                } else {
                    while (!"".equals(line)) {
                        line = br.readLine();

                        if (line == null || line.equals("")) {
                            break;
                        }

                        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                        headers.put(pair.getKey(), pair.getValue());
                    }

                    if (path.startsWith("/user/create")) {
                        String queryPath = HeaderPathUtils.extractPath(line);
                        String query = HeaderPathUtils.extractQuery(queryPath);
                        User user = new User(query);
                        log.debug("user : {}", user);

                        DataBase.addUser(user);
                        User foundUser = DataBase.findUserById(user.getUserId());
                        log.debug("found user : {}", foundUser);

                        path = "/user/list.html";
                    } else if (path.startsWith("/user/list")) {
                        String cookieHeader = headers.get("Cookie");
                        if (cookieHeader == null) {
                            path = "/user/login.html";
                            isPost = true;
                        } else {
                            HttpRequestUtils.Pair pair = HttpRequestUtils.getKeyValue(cookieHeader, "=");

                            if (pair.getValue().equals("false")) {
                                path = "/user/login.html";
                                isPost = true;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            byte[] body = null;
            if (path.startsWith("/user/list")) {
                StringBuilder stringBuilder = new StringBuilder();
                appendStringBuilder(stringBuilder, "<!DOCTYPE html>\n" +
                        "<html lang=\"kr\">\n" +
                        "<head>\n" +
                        "  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                        "  <meta charset=\"utf-8\">\n" +
                        "  <title>SLiPP Java Web Programming</title>\n" +
                        "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                        "  <link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                        "  <!--[if lt IE 9]>\n" +
                        "  <script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n" +
                        "  <![endif]-->\n" +
                        "  <link href=\"../css/styles.css\" rel=\"stylesheet\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<nav class=\"navbar navbar-fixed-top header\">\n" +
                        "  <div class=\"col-md-12\">\n" +
                        "    <div class=\"navbar-header\">\n" +
                        "\n" +
                        "      <a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n" +
                        "      <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n" +
                        "        <i class=\"glyphicon glyphicon-search\"></i>\n" +
                        "      </button>\n" +
                        "\n" +
                        "    </div>\n" +
                        "    <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n" +
                        "      <form class=\"navbar-form pull-left\">\n" +
                        "        <div class=\"input-group\" style=\"max-width:470px;\">\n" +
                        "          <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n" +
                        "          <div class=\"input-group-btn\">\n" +
                        "            <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i>\n" +
                        "            </button>\n" +
                        "          </div>\n" +
                        "        </div>\n" +
                        "      </form>\n" +
                        "      <ul class=\"nav navbar-nav navbar-right\">\n" +
                        "        <li>\n" +
                        "          <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n" +
                        "          <ul class=\"dropdown-menu\">\n" +
                        "            <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n" +
                        "            <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n" +
                        "          </ul>\n" +
                        "        </li>\n" +
                        "        <li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n" +
                        "      </ul>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</nav>\n" +
                        "<div class=\"navbar navbar-default\" id=\"subnav\">\n" +
                        "  <div class=\"col-md-12\">\n" +
                        "    <div class=\"navbar-header\">\n" +
                        "      <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\"\n" +
                        "         data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home\n" +
                        "        <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small>\n" +
                        "      </a>\n" +
                        "      <ul class=\"nav dropdown-menu\">\n" +
                        "        <li><a href=\"../user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a>\n" +
                        "        </li>\n" +
                        "        <li class=\"nav-divider\"></li>\n" +
                        "        <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n" +
                        "      </ul>\n" +
                        "\n" +
                        "      <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n" +
                        "        <span class=\"sr-only\">Toggle navigation</span>\n" +
                        "        <span class=\"icon-bar\"></span>\n" +
                        "        <span class=\"icon-bar\"></span>\n" +
                        "        <span class=\"icon-bar\"></span>\n" +
                        "      </button>\n" +
                        "    </div>\n" +
                        "    <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n" +
                        "      <ul class=\"nav navbar-nav navbar-right\">\n" +
                        "        <li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n" +
                        "        <li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n" +
                        "        <li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n" +
                        "        <li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
                        "        <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n" +
                        "      </ul>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "\n" +
                        "<div class=\"container\" id=\"main\">\n" +
                        "  <div class=\"col-md-10 col-md-offset-1\">\n" +
                        "    <div class=\"panel panel-default\">\n" +
                        "      <table class=\"table table-hover\">\n" +
                        "        <thead>\n" +
                        "        <tr>\n" +
                        "          <th>#</th>\n" +
                        "          <th>사용자 아이디</th>\n" +
                        "          <th>이름</th>\n" +
                        "          <th>이메일</th>\n" +
                        "          <th></th>\n" +
                        "        </tr>\n" +
                        "        </thead>\n" +
                        "        <tbody>");

                Collection<User> userCollection = DataBase.findAll();
                if (!userCollection.isEmpty()) {
                    int count = 1;
                    for (User user : userCollection) {
                        log.debug("user : {}", user);

                        stringBuilder.append(String.format("        <tr>\n" +
                                "          <th scope=\"row\">%d</th>\n" +
                                "          <td>%s</td>\n" +
                                "          <td>%s</td>\n" +
                                "          <td>%s</td>\n" +
                                "          <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                                "        </tr>", count++, user.getUserId(), user.getName(), user.getEmail()));
                    }
                }

                appendStringBuilder(stringBuilder, "</tbody>\n" +
                        "      </table>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "\n" +
                        "<!-- script references -->\n" +
                        "<script src=\"../js/jquery-2.2.0.min.js\"></script>\n" +
                        "<script src=\"../js/bootstrap.min.js\"></script>\n" +
                        "<script src=\"../js/scripts.js\"></script>\n" +
                        "</body>\n" +
                        "</html>");

                body = stringBuilder.toString().getBytes();
            } else {
                body = Files.readAllBytes(new File("./webapp" + path).toPath());
            }

            DataOutputStream dos = new DataOutputStream(out);
            if (path.endsWith(".css")) {
                response200HeaderCss(dos, body.length);
            } else if (isPost) {
                response302Header(dos, path, isLogin, isLoginSucess);
            } else {
                response200Header(dos, body.length);
            }
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void appendStringBuilder(StringBuilder stringBuilder, String s) {
        stringBuilder.append(s);
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

    private void response200HeaderCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location, boolean isLogin, boolean loginSuccess) {
        if (isLogin) {
            try {
                dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
                dos.writeBytes("Set-Cookie: logined=" + Boolean.toString(loginSuccess) + "; Path=/\r\n");
                dos.writeBytes("Location: " + location + "\r\n");
                dos.writeBytes("\r\n");
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            return;
        }

        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
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
