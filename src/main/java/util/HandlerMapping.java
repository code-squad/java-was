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
        parameterHandlerMap.put(new URLInfo("/create", "GET"), urlInfo -> createConstructor(User.class, (URLInfo) urlInfo));
        parameterHandlerMap.put(new URLInfo("/index.htnl", "GET"), null);
    }

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

    public static String obtainField(String methodName) {
        StringBuilder sb = new StringBuilder(methodName).replace(0, 3, "");
        sb.replace(0, 1, String.valueOf(sb.toString().charAt(0)).toLowerCase());
        return sb.toString();
    }

    public static Object saveData(URLInfo urlInfo) {
        return parameterHandlerMap.get(urlInfo).apply(urlInfo);
    }
}
