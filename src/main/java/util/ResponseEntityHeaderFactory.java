package util;

import model.RequestEntity;
import model.ResponseEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResponseEntityHeaderFactory {
    /*
       @param
       @return 리턴하는 페이지의 HTML 태그를 읽어서 반환
    */
    public static ResponseEntity of(RequestEntity requestHeader) {
        String path = ViewResolver.obtainReturnView(requestHeader);
        byte[] body = new byte[0];
        try {
            body = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(obtainStatusCode(path), obtainPath(path), body);
    }

    public static int obtainStatusCode(String path) {
        if(path.contains("redirect:")) {
            return 302;
        }
        return 200;
    }

    public static String obtainPath(String path) {
        if(path.contains("redirect:")) {
            return path.split(":")[1];
        }
        return "";
    }
}
