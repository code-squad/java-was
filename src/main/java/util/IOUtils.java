package util;

import org.slf4j.Logger;

import java.io.*;

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

}