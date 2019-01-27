package webserver;

import model.Mapping;
import model.MethodType;
import model.RequestEntity;
import model.User;
import org.reflections.Reflections;
import org.slf4j.Logger;
import security.ClientSession;
import security.HttpSession;
import setting.Controller;
import setting.GetMapping;
import setting.PostMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
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

    /* 요청 Mapping 정보에 따라 호출되는 메소드를 매핑 */
    static {
        Reflections reflections = new Reflections("webserver");

        /* setting.MainController 어노테이션 붙어있는 클래스만 확인! */
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            /* Get, Post Method 만 등록!
                    --> [질문] 상속관계를 통해 중복성 제거를 원함! How to Do..?!
            */
            registerGetMethod(clazz, obtainMethodStream(clazz, "GetMapping"));
            registerPostMethod(clazz, obtainMethodStream(clazz, "PostMapping"));

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

    /*
       @param  @Annotation PostMapping 을 가진 메소드의 스트림
       @return Void 메소드 등록!
    */
    public static void registerPostMethod(Class clazz, Stream<Method> stream) {
        List<Method> postMethods = stream.collect(Collectors.toList());
        for (Method postMethod : postMethods) {
            PostMapping postMapping = postMethod.getAnnotation(PostMapping.class);
            handlerMapper.put(new Mapping(postMapping.value(), MethodType.POST)
                    , (body, jSessionId) -> invokeMethod(createAllParameters(body, postMethod, jSessionId), clazz, postMethod));
        }
    }

    /*
       @param  @Annotation PostMapping 을 가진 메소드의 스트림
       @return Void 메소드 등록!
    */
    public static void registerGetMethod(Class clazz, Stream<Method> stream) {
        List<Method> getMethods = stream.collect(Collectors.toList());
        for (Method getMethod : getMethods) {
            GetMapping getMapping = getMethod.getAnnotation(GetMapping.class);
            handlerMapper.put(new Mapping(getMapping.value(), MethodType.GET)
                    , (body, jSessionId) -> invokeMethod(createAllParameters(body, getMethod, jSessionId), clazz, getMethod));
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
        int index = 0;
        for (Parameter parameter : parameters) {
            Class clazz = parameter.getType();
            try {
                /*[개선 필요] 중복제거 필요! -> Map + Lambda 활용하면 제거 가능할 듯..?! */
                /* String 객체 */
                if(clazz.newInstance() instanceof String) {
                    int i = 0;
                    for (String key : body.keySet()) {
                        /* String Type 은 옵션을 부여하지 않으면, args0 ~ n 형식으로 동작하기 때문에 아래와 같은 로직 필요 */
                        if(i == index) {
                            args.add(body.get(key));
                            index++;
                        }
                        i++;
                    }
                }

                /* HttpSession 객체 */
                if(clazz.newInstance() instanceof HttpSession) {
                    args.add(new HttpSession(jSessionId));
                }

                /* Model 객체 */
                if(clazz.newInstance() instanceof Model) {
                    args.add(new Model(jSessionId));
                }

                /* 객체 */
                if(!(clazz.newInstance() instanceof String || clazz.newInstance() instanceof HttpSession)) {
                    args.add(createParameterObject(clazz, body));
                }

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return args;
    }

    /*
       @param  리턴하는 클래스 타입, URL 객체
       @return 리플랙션으로 URL path 필드에 해당하는 setter 메소드를 호출하여 객체 반환 (단, 자바빈 규약 준수)
    */
    public static <T> T createParameterObject(Class clazz, Map<String, String> params) {
        T obj = null;
        try {
            obj = (T) clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().startsWith("set")) {
                    String field = obtainField(method.getName());
                    method.invoke(obj, params.get(field));
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Optional<Annotation> hasAnnotation(Annotation[] annotations, String name) {
        return Stream.of(annotations).filter(a -> a.annotationType().getSimpleName().contains(name)).findFirst();
    }

    public static String readParameter(RequestEntity requestEntity, String paramName) {
        return requestEntity.obtainParamElement(paramName);
    }

    public static String processHandler(Mapping mapping, Map<String, String> body, String jSessionId) {
        if(mapping.isResource()) {
            body.put("filePath", mapping.getPath());
            return handlerMapper.get(new Mapping("/css", MethodType.GET)).apply(body, jSessionId);
        }
        return handlerMapper.get(mapping).apply(body, jSessionId);
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
}
