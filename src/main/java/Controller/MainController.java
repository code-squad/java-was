package Controller;

import model.ResponseMessage;

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

}
