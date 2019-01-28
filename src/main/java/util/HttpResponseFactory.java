package util;

import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import webserver.viewresolver.ClientModel;
import webserver.viewresolver.ViewResolver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpResponseFactory {

    private static final Logger logger = getLogger(HttpResponseFactory.class);

    private static final String REDIRECT_KEYWORD = "redirect:";

    /*
       @param
       @return 리턴하는 페이지의 HTML 태그를 읽어서 반환
    */
    public static HttpResponse of(HttpRequest httpRequest, String view, ClientModel clientModel)
            throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String path = ViewResolver.obtainPath(view);
        byte[] body = new byte[0];
        if(!isRedirect(view)) {
            String a = readBody(path, clientModel);
            body = a.getBytes();
        }

        HttpResponse responseEntity = new HttpResponse(body);
        String status = obtainStatusCode(view);
        responseEntity.addHeader("status", status);

        if(isRedirect(view)) {
            responseEntity.addHeader("Location", ViewResolver.obtainRemovePath(view));
        }

        responseEntity.addHeader("Set-Cookie", String.format("%s; Path=/",httpRequest.obtainHeader("Cookie")));

        return responseEntity;
    }

    public static String readBody(String path, ClientModel clientModel)
            throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        StringBuilder sb = new StringBuilder();
        String line = "";
        List<String> accumulate = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        while((line = br.readLine()) != null) {
            sb.append(processMustache(line, clientModel, accumulate));
        }
        return sb.toString();
    }

    public static String processMustache(String line, ClientModel clientModel, List<String> accumulate)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return IOUtils.extractRegex(line, accumulate, clientModel);
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
