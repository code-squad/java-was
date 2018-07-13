package util;

public class HeaderPathUtils {

    public static String extractPath(String line) {
        String[] paths = line.split(" ");
        return paths[1];
    }

    public static boolean checkQueryString(String path) {
        return path.contains("?");
    }

    public static String extractRealPath(String path) {
        return path.split("\\?")[0];
    }

    public static String extractQuery(String path) {
        return path.split("\\?")[1];
    }

    public static boolean checkPost(String line) {
        return line.split(" ")[0].equals("POST");
    }
}
