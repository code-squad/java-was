package webserver;

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
            viewMappingResolver.put(new RequestEntity("/users/create", "GET", "", null), obtainFullPath("user/form.html"));
            /* 메인화면 이동 */
            viewMappingResolver.put(new RequestEntity("/index.html", "GET", "", null), obtainFullPath("index.html"));
            /* 회원가입 POST */
            viewMappingResolver.put(new RequestEntity("/users/create", "POST", "", null), obtainRedirectPath("/index.html"));
            /* 회원가입 이동 */
            viewMappingResolver.put(new RequestEntity("/users/form", "GET", "", null), obtainFullPath("user/form.html"));
            /* 로그인 이동 */
            viewMappingResolver.put(new RequestEntity("/users/login", "GET", "", null), obtainFullPath("user/login.html"));
            /* 로그인 성공 POST */
            viewMappingResolver.put(new RequestEntity("/users/login", "POST", "", null), obtainRedirectPath("/index.html"));
            /* 로그인 실패 GET */
            viewMappingResolver.put(new RequestEntity("/users/login_failed", "GET", "", null), obtainFullPath("user/login_failed.html"));
            /* 회원리스트 GET */
            viewMappingResolver.put(new RequestEntity("/users/list", "GET", "", null), obtainFullPath("user/list.html"));

        }

    public static String obtainFullPath(String path) {
        return String.format("%s/%s", root, path);
    }

    public static String obtainRedirectPath(String path) {
        return String.format("redirect:%s", path);
    }

    public static String obtainRemoveRedirectFullPath(String path) {
        if(path.contains("redirect:")) {
            return obtainFullPath(obtainRemovePath(path));
        }
        return path;
    }

    public static String obtainRemovePath(String path) {
        return path.split("redirect:")[1];
    }

    public static String obtainReturnView(RequestEntity requestEntity) {
        return viewMappingResolver.get(requestEntity);
    }
}
