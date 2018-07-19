package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class IOUtils {
    public static final String CONTENT_LENGTH = "Content-Length";

    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static Optional<String> readData(BufferedReader br, String contentLength) throws IOException {
        int length = Integer.parseInt(contentLength);
        if (length == 0) {
            return Optional.empty();
        }
        char[] body = new char[length];
        br.read(body, 0, length);
        return Optional.of(String.copyValueOf(body));
    }
}
