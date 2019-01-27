package util;

import org.junit.Test;
import org.slf4j.Logger;
import webserver.controller.MainController;
import webserver.handlermapping.HandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.slf4j.LoggerFactory.getLogger;

public class ReflectionTest {

    private static final Logger logger = getLogger(ReflectionTest.class);

    @Test
    public void 어노테이션_Test() {
        Class clazz = HandlerMapping.class;
        Method[] methods = clazz.getDeclaredMethods();

        for(Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                logger.debug("Annotation : {}", annotation.annotationType().getSimpleName());
            }
        }
    }

    @Test
    public void 어노테이션2_Test() {
        Class clazz = MainController.class;
        Method[] methods = clazz.getDeclaredMethods();
    }
}
