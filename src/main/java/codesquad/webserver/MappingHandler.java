package codesquad.webserver;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.HttpRequest;
import codesquad.model.HttpSession;
import codesquad.model.Url;
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

    public static boolean hasMappingPath(HttpRequest httpRequest) {
        return httpRequest.hasMappingUrl(mappingHandler);
    }

    public static void invoke(HttpRequest httpRequest) throws Exception {
        Method thisMethod = httpRequest.findMappingMethod(mappingHandler);
        Object thisObject = httpRequest.findMappingMethod(mappingHandler).getDeclaringClass().newInstance();
        Object[] args = ParameterBinder.bind(thisMethod, httpRequest);
        Object result = thisMethod.invoke(thisObject, args);
        reflectCookie(args, httpRequest);
        httpRequest.generateResponseCode(result);
        log.debug(httpRequest.toString());
    }

    private static void reflectCookie(Object[] args, HttpRequest httpRequest) {
        Arrays.stream(args)
                .filter(arg -> (arg instanceof HttpSession))
                .forEach(arg -> {
                    HttpSession httpSession = (HttpSession)arg;
                    httpRequest.addCookie(httpSession);
                });
    }

}
