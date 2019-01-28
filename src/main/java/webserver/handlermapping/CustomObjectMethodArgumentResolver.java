package webserver.handlermapping;

import org.reflections.Reflections;
import security.HttpSession;
import setting.ArgumentResolver;
import webserver.viewresolver.Model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@ArgumentResolver
public class CustomObjectMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(Class clazz) throws IllegalAccessException, InstantiationException {
        return !((clazz.newInstance() instanceof String || clazz.newInstance() instanceof HttpSession || clazz.newInstance() instanceof Model));
    }

    @Override
    public Object resolveArgument(Class clazz, String jSessionId, Map<String, String> body) {
        Object obj = null;
        try {
            obj =  clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")) {
                    String field = obtainField(method.getName());
                    method.invoke(obj, body.get(field));
                    body.remove(field);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public boolean identification(Class clazz) {
        return Object.class == clazz;
    }

    /*
       @param  setter 메소드 이름
       @return setter 메소드 이름에서 필드값 추출 후 반환
    */
    private static String obtainField(String methodName) {
        StringBuilder sb = new StringBuilder(methodName).replace(0, 3, "");
        sb.replace(0, 1, String.valueOf(sb.toString().charAt(0)).toLowerCase());
        return sb.toString();
    }
}
