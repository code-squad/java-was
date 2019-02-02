package controller;

import dto.ResponseMessage;
import util.HttpResponseHeaderUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainController {

    public static ResponseMessage showHtml(String path) throws IOException {
        return ResponseMessage.ofBody(Files.readAllBytes(new File("./webapp" + path).toPath()));
    }

    public static ResponseMessage showDefaultMessage() {
        return ResponseMessage.ofDefault();
    }

    public static ResponseMessage showCss(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        return ResponseMessage.ofMessage(HttpResponseHeaderUtils.generate200CssHeader(body.length), body);
    }
}
