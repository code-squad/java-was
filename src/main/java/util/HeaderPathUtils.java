package util;

public class HeaderPathUtils {

    public static String extractPath(String line) {
        String[] paths = line.split(" ");
        return paths[1];
    }
}
