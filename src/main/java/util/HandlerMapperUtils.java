package util;

public class HandlerMapperUtils {

    public static String parseControllerName(String requestPath) {
        int secondSlash = requestPath.replaceFirst("/", "").indexOf("/") + 1;
        if (secondSlash == 0) {
            return requestPath;
        }
        return requestPath.substring(0, secondSlash).toLowerCase();
    }
}
