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

    public static byte[] obtainBody(URLInfo urlInfo) throws IOException {
        return Files.readAllBytes(new File(urlInfo.obtainReturnFilePath()).toPath());
    }

    /*
        java.lang.IllegalArgumentException: Missing scheme 발생
            -> 원인은 'file:///' 없어서! --> 사용불가!
     */
    public static Path obtainPath(String path) {
        return Paths.get(URI.create(path));
    }

}