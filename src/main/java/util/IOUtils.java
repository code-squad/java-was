package util;

import model.URLInfo;
import org.slf4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.slf4j.LoggerFactory.getLogger;

public class IOUtils {

    private static final Logger logger = getLogger(IOUtils.class);

    /*
       @param BufferedReader는 Request Body를 시작하는 시점이어야
       @param contentLength는 Request Header의 Content-Length 값이다.
       @return
       @throws IOException
    */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    /*
       @param
       @return 리턴하는 페이지의 HTML 태그를 읽어서 반환
    */
    public static byte[] obtainBody(URLInfo urlInfo) throws IOException {
        return Files.readAllBytes(new File(ViewResolver.obtainReturnView(urlInfo)).toPath());
    }
}