package codesquad.webserver;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.Url;
import codesquad.util.responses.ResponseCode;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class MappingHandler {
    private static final Logger log = getLogger(MappingHandler.class);

    public static final String ROOT_PACKAGE = "codesquad";
    private static final Map<Url, Method> mappingHandler = new HashMap<>();

    static {
        init();
    }

    private static void init() {
        Set<Class<?>> controllers = new Reflections(ROOT_PACKAGE).getTypesAnnotatedWith(Controller.class);
        controllers.stream().forEach(controllerClass -> checkMethod(controllerClass));
    }

    private static void checkMethod(Class<?> controllerClass) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(aMethod -> aMethod.isAnnotationPresent(RequestMapping.class))
                .forEach(aMethod -> {
                    RequestMapping requestMapping = aMethod.getAnnotation(RequestMapping.class);
                    mappingHandler.put(new Url(requestMapping.method(), requestMapping.value()), aMethod);
                });
    }

    public static boolean hasMappingPath(Url url) {
        return mappingHandler.containsKey(url);
    }

    public static ResponseCode invoke(Url url) throws Exception {
        Method thisMethod = mappingHandler.get(url);
        Object thisObject = mappingHandler.get(url).getDeclaringClass().newInstance();
        Object[] args = ParameterBinder.bind(thisMethod, url);
        Object result = thisMethod.invoke(thisObject, args);
        return generateResponseCode(url, result);
    }

    public static ResponseCode generateResponseCode(Url url, Object result) {
        String newAccessPath = (String) result;
        if(newAccessPath.contains("redirect")) {
            url.renewAccessPath(newAccessPath.split(":")[1]);
            return ResponseCode.FOUND;
        }
        url.renewAccessPath(newAccessPath);
        return ResponseCode.OK;
    }
}
