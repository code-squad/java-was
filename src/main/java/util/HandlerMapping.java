package util;

import model.RequestEntity;
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

    private static Map<RequestEntity, UnaryOperator> parameterHandlerMap = new HashMap<>();

    static {
        /* 회원가입 GET */
        parameterHandlerMap.put(new RequestEntity("/users/create", "GET", "")
                , requestHeader -> createConstructor(User.class, (RequestEntity) requestHeader));
        /* 메인화면 이동 */
        parameterHandlerMap.put(new RequestEntity("/index.html", "GET", ""), null);
        /* 회원가입 POST */
        parameterHandlerMap.put(new RequestEntity("/users/create", "POST", "")
                , requestHeader -> createConstructor(User.class, (RequestEntity) requestHeader));
        /* 회원가입 이동 */
        parameterHandlerMap.put(new RequestEntity("/users/form", "GET", ""), null);
        /* 로그인 이동 */
        parameterHandlerMap.put(new RequestEntity("/users/login", "GET", ""), null);
        /* 로그인 POST */
        parameterHandlerMap.put(new RequestEntity("/users/login", "POST", ""), null);
    }

    /*
       @param  리턴하는 클래스 타입, URL 객체
       @return 리플랙션으로 URL path 필드에 해당하는 setter 메소드를 호출하여 객체 반환 (단, 자바빈 규약 준수)
    */
    public static <T> T createConstructor(Class clazz, RequestEntity requestEntity) {
        T obj = null;
        try {
            obj = (T) clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")) {
                    String field = obtainField(method.getName());
                    method.invoke(obj, requestEntity.obtainParamElement(field));
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

    public static Object saveData(RequestEntity requestHeader) {
        if(parameterHandlerMap.get(requestHeader) != null) {
            return parameterHandlerMap.get(requestHeader).apply(requestHeader);
        }
        return null;
    }
}
