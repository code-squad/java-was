package controller;

import db.DataBase;
import dto.ResponseMessage;
import model.User;
import org.slf4j.Logger;
import util.HttpRequestUtils;
import util.HttpResponseHeaderUtils;
import util.HttpResponseHtmlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class UserController {
    private static final Logger log = getLogger(UserController.class);

    public static ResponseMessage create(BufferedReader br, String headerFirstLine) throws IOException {
        Map<String, String> header = HttpRequestUtils.readHeader(br, headerFirstLine);
        Map<String, String> body = HttpRequestUtils.readRequestBody(br, Integer.parseInt(header.get("Content-Length")));
        User newUser =
                User.of(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));
        DataBase.addUser(newUser);
        return ResponseMessage.ofMessage(HttpResponseHeaderUtils.generate302Header("/index.html"), "".getBytes());
    }

    public static ResponseMessage login(BufferedReader br, String headerFirstLine) throws IOException {
        Map<String, String> header = HttpRequestUtils.readHeader(br, headerFirstLine);
        Map<String, String> parseValues = HttpRequestUtils.readRequestBody(br, Integer.parseInt(header.get("Content-Length")));
        User maybeUser = DataBase.findUserById(parseValues.get("userId"));
        if(maybeUser.isEmpty()) return ResponseMessage.ofMessage(HttpResponseHeaderUtils.generate302CookieHeader("/user/login_failed.html", false), "".getBytes());
        if(maybeUser.isCorrect(parseValues.get("password"))){
            return ResponseMessage.ofMessage(HttpResponseHeaderUtils.generate302CookieHeader("/index.html", true), "".getBytes());
        }
        return ResponseMessage.ofMessage(HttpResponseHeaderUtils.generate302CookieHeader("/user/login_failed.html", false), "".getBytes());
    }

    public static ResponseMessage list(BufferedReader br, String headerFirstLine) throws IOException {
        Map<String, String> header = HttpRequestUtils.readHeader(br, headerFirstLine);
        if(isLogin(header)) {
            return ResponseMessage.ofBody(HttpResponseHtmlUtils.generate(DataBase.findAll()).getBytes());
        }
        return ResponseMessage.ofMessage(HttpResponseHeaderUtils.generate302Header("/user/login.html"), "".getBytes());
    }

    private static boolean isLogin(Map<String, String> header) {
        if(header.get("Cookie") != null){
            String[] cookie = header.get("Cookie").split("=");
            if(cookie.length == 2) {
                return Boolean.parseBoolean(cookie[1]);
            }
        }
        return false;
    }
}
