package codesquad.webserver;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.HttpSession;
import codesquad.model.Header;
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

    public static boolean hasMappingPath(Url url) {
        return mappingHandler.containsKey(url);
    }

    public static void invoke(Header header) throws Exception {
        Url url = header.getUrl();
        Method thisMethod = mappingHandler.get(url);
        Object thisObject = mappingHandler.get(url).getDeclaringClass().newInstance();
        Object[] args = ParameterBinder.bind(thisMethod, url);
        Object result = thisMethod.invoke(thisObject, args);

        Arrays.stream(args)
                .filter(arg -> arg.getClass().getName().equals("codesquad.model.HttpSession"))
                .forEach(arg -> {
                    HttpSession httpSession = (HttpSession)arg;
                    header.addCookie(httpSession);
                });
        log.debug(header.toString());

        header.generateResponseCode(result);
    }

}
