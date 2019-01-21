package util;

import org.checkerframework.checker.nullness.Opt;

import java.util.Optional;

public class PathReaderUtils {
    private static String GET_METHOD = "GET";
    private static String SUFFIX_HTML = "html";

    public static Optional<String> extractURL(String header) {
        String[] tokens = header.split(" ");
        if(tokens[0].equals(GET_METHOD) && tokens[1].endsWith(SUFFIX_HTML)) {
            return Optional.of(tokens[1]);
        }
        return Optional.empty();
    }
}
