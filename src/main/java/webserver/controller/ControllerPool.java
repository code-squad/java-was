package webserver.controller;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.RequestMapping;
import webserver.request.Request;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ControllerPool<T extends Controller> {
    private static final Logger log = LoggerFactory.getLogger(ControllerPool.class);

    private static ControllerPool container = new ControllerPool();
    private final Class<T> viewControllerClass = (Class<T>) ViewController.class;

    private List<Class<T>> controllers = ImmutableList.of((Class<T>) UserController.class);
    private final Map<Class<T>, T> pool;

    private ControllerPool() {
        pool = new HashMap<>();
        controllers.forEach(controllerClass -> pool.put(controllerClass, null));
    }

    public static ControllerPool of() {
        return container;
    }

    public T search(Request request) {
        String searchUri = request.getFirstUri();
        Optional<Class<T>> maybeControllerClass = controllers.stream().filter(clazz -> clazz.isAnnotationPresent(RequestMapping.class)).filter(clazz -> clazz.getAnnotation(RequestMapping.class).value().contains(searchUri)).findFirst();
        if (!maybeControllerClass.isPresent()) {
            return get(viewControllerClass);

        }

        Class<T> controllerClass = maybeControllerClass.get();
        String excludeParams = request.getUriExcludeParams();
        boolean isMatch = Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .map(method -> method.getAnnotation(RequestMapping.class))
                .anyMatch(annotation -> String.valueOf(controllerClass.getAnnotation(RequestMapping.class).value() + annotation.value()).equals(excludeParams));

        return isMatch ? get(controllerClass) : get(viewControllerClass);
    }

    private T get(Class<T> controllerClass) {
        T controller = pool.get(controllerClass);
        if (Objects.isNull(controller)) {
            return create(controllerClass);
        }
        return controller;
    }

    private T create(Class<T> controllerClass) {
        try {
            Constructor<T> constructor = controllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            T controller = constructor.newInstance();
            constructor.setAccessible(false);
            pool.put(controllerClass, controller);
            return controller;
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            log.error(e.getMessage());
            System.exit(1);
            return null;
        }
    }
}
