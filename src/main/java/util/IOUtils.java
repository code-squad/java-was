package util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    private static final int INDEX_PATH = 1;
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String parsePath(String line) throws IOException {
        if (line == null) throw new IOException("잘못된 Request Start Line");
        return line.split(" ")[INDEX_PATH];
    }
}
