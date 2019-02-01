package controller;

import db.DataBase;
import dto.ResponseMessage;
import model.User;
import org.slf4j.Logger;
import util.HttpRequestUtils;
import util.ResponseHeaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class UserController {
    private static final Logger log = getLogger(UserController.class);


    public static ResponseMessage create(BufferedReader br, String headerFirstLine) throws IOException {
        Map<String, String> parseValues = HttpRequestUtils.readRequestBody(br, headerFirstLine);
        User newUser =
                User.of(parseValues.get("userId"), parseValues.get("password"), parseValues.get("name"), parseValues.get("email"));
        DataBase.addUser(newUser);
        return ResponseMessage.ofMessage(ResponseHeaderUtils.generate302Header("/index.html"), "".getBytes());
    }

    public static ResponseMessage login(BufferedReader br, String headerFirstLine) throws IOException {
        Map<String, String> parseValues = HttpRequestUtils.readRequestBody(br, headerFirstLine);
        User maybeUser = DataBase.findUserById(parseValues.get("userId"));
        if(maybeUser.isEmpty()) return ResponseMessage.ofMessage(ResponseHeaderUtils.generate302Header("/user/login_failed.html"), "".getBytes());
        if(maybeUser.isCorrect(parseValues.get("password"))){
            return ResponseMessage.ofMessage(ResponseHeaderUtils.generate302Header("/index.html"), "".getBytes());
        }
        return ResponseMessage.ofMessage(ResponseHeaderUtils.generate302Header("/user/login_failed.html"), "".getBytes());
    }
}
