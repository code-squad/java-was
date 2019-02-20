package fixture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class PostQuery {
    static private String getRequest =
            "POST /user/create HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "Content-Length: 27\n" +
                    "Cache-Control: max-age=0\n" +
                    "Origin: http://localhost:8080\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                    "Referer: http://localhost:8080/user/form.html\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n\nuserId=id&password=password";
    static public InputStream get(){
        try {
            return new ByteArrayInputStream(PostQuery.getRequest.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
