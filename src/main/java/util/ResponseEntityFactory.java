package util;

import model.RequestEntity;
import model.ResponseEntity;
import org.slf4j.Logger;
import webserver.ClientModel;
import webserver.ViewResolver;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

public class ResponseEntityFactory {

    private static final Logger logger = getLogger(ResponseEntityFactory.class);

    private static final String REDIRECT_KEYWORD = "redirect:";

    /*
       @param
       @return 리턴하는 페이지의 HTML 태그를 읽어서 반환
    */
    public static ResponseEntity of(RequestEntity requestEntity, String view, ClientModel clientModel) throws IOException {
        String path = ViewResolver.obtainRemoveRedirectFullPath(view);
        byte[] body = new byte[0];
        if(!isRedirect(view)) {
            body = Files.readAllBytes(Paths.get(path));
        }

        ResponseEntity responseEntity = new ResponseEntity(body);
        String status = obtainStatusCode(view);
        responseEntity.addHeader("status", status);

        if(isRedirect(view)) {
            responseEntity.addHeader("Location", ViewResolver.obtainRemovePath(view));
        }

        responseEntity.addHeader("Set-Cookie", String.format("%s; Path=/",requestEntity.obtainCookie()));

        return responseEntity;
    }

    public static String readBody(String path, ClientModel clientModel) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        File view = new File(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));

        return sb.toString();
    }

    public static String obtainStatusCode(String path) {
        if(isRedirect(path)) {
            return "302";
        }

        return "200";
    }

    public static boolean isRedirect(String path) {
        return path.contains(REDIRECT_KEYWORD);
    }
}
