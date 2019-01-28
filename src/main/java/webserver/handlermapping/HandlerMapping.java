package webserver.handlermapping;

import model.HttpRequest;
import model.Mapping;
import model.MethodType;
import org.reflections.Reflections;
import org.slf4j.Logger;
import setting.Controller;
import setting.GetMapping;
import setting.PostMapping;
import webserver.WebMvcConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public class HandlerMapping {

    private static final Logger logger = getLogger(HandlerMapping.class);

    private static Map<Mapping, BiFunction<Map, String, String>> handlerMapper = new HashMap<>();

    private static Map<Class, Function<Method, String>> methodMapper = new HashMap<>();

    private static WebMvcConfig webMvcConfig;

    /* 요청 Mapping 정보에 따라 호출되는 메소드를 매핑 */
    static {
        webMvcConfig = new WebMvcConfig();
        webMvcConfig.initHandlerMethodArgumentResolvers();

        methodMapper.put(GetMapping.class, (method) -> method.getAnnotation(GetMapping.class).value());
        methodMapper.put(PostMapping.class, (method) -> method.getAnnotation(PostMapping.class).value());

        Reflections reflections = new Reflections("webserver");
        /* setting.MainController 어노테이션 붙어있는 클래스만 확인! */
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            registerMethod(clazz);
        }
    }

    /*
       @param  @Annotation 이름
       @return @Annotation 이름이 붙은 모든 메소드의 스트림을 반환
    */
    public static Stream<Method> obtainMethodStream(Class clazz, String name) {
        return Stream.of(clazz.getDeclaredMethods())
                .filter(m -> hasAnnotation(m.getDeclaredAnnotations(), name).isPresent());
    }

    public static void registerMethod(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Class methodClazz = MethodType.obtainMethodClass(method);
            if(methodClazz != null) {
                handlerMapper.put(new Mapping(methodMapper.get(methodClazz).apply(method), MethodType.obtainMethodTypeByClass(methodClazz))
                        , (body, jSessionId) -> invokeMethod(createAllParameters(body, method, jSessionId), clazz, method));
            }
        }
    }

    public static String invokeMethod(List<Object> body, Class clazz, Method method) {
        String returnPage = "";
        try {
            Object[] objects = body.toArray();
            returnPage = (String) method.invoke(clazz.newInstance(), objects);
        } catch (Exception e) {

        }
        return returnPage;
    }

    public static List<Object> createAllParameters(Map<String, String> body, Method method, String jSessionId)  {
        Parameter[] parameters = method.getParameters();
        List<Object> args = new ArrayList<>();
        for (Parameter parameter : parameters) {
            try {
                Class clazz = parameter.getType();
                Object object = webMvcConfig.obtainHandlerMethodArgumentResolver(clazz).resolveArgument(clazz, jSessionId, body);
                args.add(object);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return args;
    }

    public static Optional<Annotation> hasAnnotation(Annotation[] annotations, String name) {
        return Stream.of(annotations).filter(a -> a.annotationType().getSimpleName().contains(name)).findFirst();
    }

    public static String readParameter(HttpRequest httpRequest, String paramName) {
        return httpRequest.obtainParamElement(paramName);
    }

    public static String processHandler(Mapping mapping, Map<String, String> body, String jSessionId) {
        if(mapping.isResource()) {
            body.put("filePath", mapping.getPath());
            return handlerMapper.get(new Mapping("/css", MethodType.obtainMethodTypeByName("GET"))).apply(body, jSessionId);
        }
        return handlerMapper.get(mapping).apply(body, jSessionId);
    }
}
