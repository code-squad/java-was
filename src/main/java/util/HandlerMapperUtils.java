package util;

import exception.HandlerMappingException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerMapperUtils {

    public static String parseControllerName(String requestPath) {
        if (requestPath.equals("/")) {
            return requestPath;
        }

        Pattern pattern = Pattern.compile("\\/(.*?)\\/");
        Matcher matcher = pattern.matcher(requestPath);
        if (!matcher.find()) {
            throw new HandlerMappingException();
        }
        return matcher.group(matcher.start()).replaceAll("/", "");
    }
}
