package webserver;

import db.DataBase;
import model.RequestEntity;
import model.User;
import org.slf4j.Logger;
import security.ClientSession;
import security.HttpSession;
import service.UserService;
import setting.Controller;
import setting.GetMapping;
import setting.PostMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class HandlerMapping {

    private static final Logger logger = getLogger(HandlerMapping.class);

    private static Map<RequestEntity, UnaryOperator<RequestEntity>> parameterHandlerMap = new HashMap<>();

    static {
        /* 회원가입 GET */
        parameterHandlerMap.put(new RequestEntity("/users/create", "GET", "", null)
                , requestHeader -> createConstructor(User.class, requestHeader));
        /* [메인화면 이동] */
        parameterHandlerMap.put(new RequestEntity("/index.html", "GET", "", null), null);
        /* 회원가입 POST */
        parameterHandlerMap.put(new RequestEntity("/users/create", "POST", "", null)
                , requestEntity -> saveUser(requestEntity));
        /* 회원가입 이동 */
        parameterHandlerMap.put(new RequestEntity("/users/form", "GET", "", null), null);
        /* 로그인 이동 */
        parameterHandlerMap.put(new RequestEntity("/users/login", "GET", "", null), null);
        /* 회원리스트 GET */
        parameterHandlerMap.put(new RequestEntity("/users/list", "GET", "", null)
                , requestEntity -> showList(requestEntity));
        /* 로그인 POST */
        parameterHandlerMap.put(new RequestEntity("/users/login", "POST", "", null)
                , requestEntity -> processLogin(requestEntity));
    }

    static {
        Class clazz = HandlerMapping.class;
        Method[] methods = clazz.getDeclaredMethods();

        for(Method method : methods) {
            for(Annotation annotation : method.getAnnotations()) {
                String annotationName = annotation.annotationType().getSimpleName();
                if(annotationName.equals())
            }
        }
    }

    @GetMapping(value = "/index.html")
    public RequestEntity navigate(RequestEntity requestEntity) {
        return requestEntity;
    }

    @PostMapping(value="/users/create")
    public String join(User user) {
        return "";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static RequestEntity processCookie(RequestEntity requestEntity) {
        ClientSession clientSession = HttpSession.getSession(requestEntity.obtainParamElement("JSESSIONID"));
        if(clientSession != null && clientSession.hasSession("loginedUser")) {
            logger.debug("Cookie 등록 완료");
            return requestEntity.addCookie("logined=true");
        }
        return requestEntity.addCookie("logined=false");
    }

    private static RequestEntity showList(RequestEntity requestEntity) {
        logger.debug("Show List Request Entity : {}", requestEntity.toString());
        if(isLoginedUser(requestEntity)) {
            return requestEntity;
        }
        return new RequestEntity("/index.html", "GET", "", null);
    }

    private static boolean isLoginedUser(RequestEntity requestEntity) {
        return requestEntity.hasLoginLoCookie();
    }

    private static RequestEntity processLogin(RequestEntity requestEntity) {
        if(isLoginSuccess(requestEntity)) {
            logger.debug("Login 성공");
            ClientSession clientSession = new ClientSession();
            clientSession.registerSession("loginedUser", createConstructor(User.class, requestEntity));
            HttpSession.addSession(requestEntity.obtainParamElement("JSESSIONID"), clientSession);
            return requestEntity;
        }
        logger.debug("Login 실패");
        return new RequestEntity("/users/login_failed", "GET", "", null);
    }

    public static boolean isLoginSuccess(RequestEntity requestEntity) {
        String userId = readParameter(requestEntity, "userId");
        String password = readParameter(requestEntity, "password");
        User registeredUser = DataBase.findUserById(userId);
        logger.debug("DB에 입력된 사용자 : {}", registeredUser.toString());

        return registeredUser.enableLogin(userId, password);
    }

    public static String readParameter(RequestEntity requestEntity, String paramName) {
        return requestEntity.obtainParamElement(paramName);
    }

    private static RequestEntity saveUser(RequestEntity requestEntity) {
        User user = (User) createConstructor(User.class, requestEntity);
        UserService.saverUser(user);
        logger.debug("회원가입 처리 완료!");

        return requestEntity;
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

    public static RequestEntity processRequest(RequestEntity requestEntity) {
        if(parameterHandlerMap.get(requestEntity) != null) {
            return parameterHandlerMap.get(requestEntity).apply(requestEntity);
        }
        return requestEntity;
    }
}
