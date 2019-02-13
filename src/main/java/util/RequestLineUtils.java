package util;

public class RequestLineUtils {
    public static String getPath(String input) {
        String[] strings = input.split(" ");
        return strings[1];
    }
}
