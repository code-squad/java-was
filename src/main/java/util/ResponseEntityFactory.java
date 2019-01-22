package util;

import model.RequestEntity;
import model.ResponseEntity;
import org.slf4j.Logger;
import webserver.ViewResolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseEntityFactory {

    private static final Logger logger = getLogger(ResponseEntityFactory.class);

    /*
       @param
       @return 리턴하는 페이지의 HTML 태그를 읽어서 반환
    */
    public static ResponseEntity of(RequestEntity requestEntity) throws IOException {
        String path = ViewResolver.obtainReturnView(requestEntity);
        logger.debug("Return Page : {}", path);
        byte[] body = Files.readAllBytes(Paths.get(ViewResolver.obtainRemoveRedirectFullPath(path)));

        ResponseEntity responseEntity = new ResponseEntity(body);
        String status = obtainStatusCode(path);
        responseEntity.addHeader("status", status);

        if(status.equals("302")) {
            responseEntity.addHeader("Location", ViewResolver.obtainRemovePath(path));
        }

        if(requestEntity.hasLoginLoCookie()) {
            responseEntity.addHeader("Set-Cookie", String.format("%s; Path=/",requestEntity.obtainCookie()));
        }

        return responseEntity;
    }

    public static String obtainStatusCode(String path) {
        if(path.contains("redirect:")) {
            return "302";
        }

        return "200";
    }
}
