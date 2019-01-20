package util;

import model.RequestEntity;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ViewResolver {
    private static Map<RequestEntity, String> viewMappingResolver = new HashMap<>();

    private static final Logger logger = getLogger(ViewResolver.class);

    private static String root = "";

    static {
        try {
            root = new File(".").getCanonicalPath() + "/webapp";
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 회원가입 GET */
        viewMappingResolver.put(new RequestEntity("/users/create", "GET", ""), obtainPath("user/form.html"));
        /* 메인화면 이동 */
        viewMappingResolver.put(new RequestEntity("/index.html", "GET", ""), obtainPath("index.html"));
        /* 회원가입 POST */
        viewMappingResolver.put(new RequestEntity("/users/create", "POST", ""), obtainRedirectPath("/index.html"));
        /* 회원가입 이동 */
        viewMappingResolver.put(new RequestEntity("/users/form", "GET", ""), obtainPath("user/form.html"));
        /* 로그인 이동 */
        viewMappingResolver.put(new RequestEntity("/users/login", "GET", ""), obtainPath("user/login.html"));
        /* 로그인 성공 POST */
        viewMappingResolver.put(new RequestEntity("/users/login", "POST", ""), obtainPath("/index.html"));

    }

    public static String obtainPath(String path) {
        return String.format("%s/%s", root, path);
    }

    public static String obtainRedirectPath(String path) {
        return String.format("redirect:%s", path);
    }

    public static String obtainReturnView(RequestEntity urlInfo) {
        logger.debug("Return view path : {}", viewMappingResolver.get(urlInfo));
        return viewMappingResolver.get(urlInfo);
    }
}
