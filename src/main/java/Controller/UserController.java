package Controller;

import db.DataBase;
import model.ResponseMessage;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import util.ResponseHeaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class UserController {

    public static ResponseMessage createUser(BufferedReader br, String headerFirstLine) throws IOException {
        int contentLength = HttpRequestUtils.readHeader(br, headerFirstLine);
        String requestBody = IOUtils.readData(br, contentLength);
        Map<String, String> parseValues = HttpRequestUtils.parseQueryString(requestBody);
        User newUser =
                new User(parseValues.get("userId"), parseValues.get("password"), parseValues.get("name"), parseValues.get("email"));
        DataBase.addUser(newUser);
        return ResponseMessage.ofMessage(ResponseHeaderUtils.generate302Header("/index.html"), "".getBytes());
    }
}
