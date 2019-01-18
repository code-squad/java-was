package util;

import model.URLInfo;
import model.User;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.slf4j.LoggerFactory.getLogger;

public class HandlerMapping {

    private static final Logger logger = getLogger(HandlerMapping.class);

    private static Map<URLInfo, UnaryOperator> parameterHandlerMap = new HashMap<>();

    static {
        parameterHandlerMap.put(new URLInfo("/users/create", "GET"), urlInfo -> createConstructor(User.class, (URLInfo) urlInfo));
        parameterHandlerMap.put(new URLInfo("/index.htnl", "GET"), null);
    }

    /*
       @param  리턴하는 클래스 타입, URL 객체
       @return 리플랙션으로 URL path 필드에 해당하는 setter 메소드를 호출하여 객체 반환 (단, 자바빈 규약 준수)
    */
    public static <T> T createConstructor(Class clazz, URLInfo urlInfo) {
        T obj = null;
        try {
            obj = (T) clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")) {
                    String field = obtainField(method.getName());
                    method.invoke(obj, urlInfo.obtainParamElement(field));
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /*
       @param  setter 메소드 이름
       @return setter 메소드 이름에서 필드값 추출 후 반환
    */
    public static String obtainField(String methodName) {
        StringBuilder sb = new StringBuilder(methodName).replace(0, 3, "");
        sb.replace(0, 1, String.valueOf(sb.toString().charAt(0)).toLowerCase());
        return sb.toString();
    }

    public static Object saveData(URLInfo urlInfo) {
        return parameterHandlerMap.get(urlInfo).apply(urlInfo);
    }
}
