package util;

import org.slf4j.Logger;
import webserver.viewresolver.ClientModel;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.slf4j.LoggerFactory.getLogger;

public class IOUtils {

    private static final Logger logger = getLogger(IOUtils.class);

    private static final String REGEX = "\\{\\{.[a-zA-z][^}]+\\}\\}";

    private static final String START_POINT = "#";

    private static final String END_POINT = "/";

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

    public static boolean isPattern(String line) {
        Matcher matcher = Pattern.compile(REGEX).matcher(line);
        return matcher.find();
    }

    public static String extractRegex(String line, List<String> accumulate, ClientModel clientModel)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Matcher matcher = Pattern.compile(REGEX).matcher(line);
        while(matcher.find()) {
            String extract = removeRegex(matcher.group());
            line = line.replace(matcher.group(), processExtract(matcher.group(), extract, accumulate, clientModel));
        }
        return line;
    }

    public static String processExtract(String matcher, String extract, List<String> accumulate, ClientModel clientModel)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        /* # 을 가지고 있으면 누적, 출력은 빈값*/
        if(extract.contains(START_POINT)) {
            accumulate.add(extract.substring(1));
        }

        /* /를 가지고 있으면 누적 제거 출력은 빈값*/
        if(extract.contains(END_POINT)) {
            accumulate.remove(extract.substring(1));
        }

        /* # / 안 가지고 있으면 누적된 것의 가장 바깥부분에 있는 객체의 값 */
        if(!(extract.contains(START_POINT) || extract.contains(END_POINT))) {
            String objectName = accumulate.get(accumulate.size() - 1);
            if(clientModel.hasMustache(objectName)) {
                Object object = clientModel.obtainAttribute(objectName);
                Method method = object.getClass().getMethod(obtainMethodName(extract));
                return valueByMethod(method, object);
            }
        }

        return "";
    }

    public static String valueByMethod(Method method, Object object) throws InvocationTargetException, IllegalAccessException {
        if (method != null && method.invoke(object) != null) {
            return (String)method.invoke(object);
        }
        return "";
    }

    public static String obtainMethodName(String fieldName) {
        return new StringBuilder().append("get").append(String.valueOf(fieldName.charAt(0)).toUpperCase())
                .append(fieldName.substring(1)).toString();
    }

    public static String removeRegex(String word) {
        return word.replace("{", "").replace("}", "");
    }
}