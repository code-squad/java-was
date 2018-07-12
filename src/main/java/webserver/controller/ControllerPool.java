package webserver.controller;

import com.google.common.collect.ImmutableList;
import webserver.annotation.RequestMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ControllerPool<T extends Controller> {
    private static ControllerPool container = new ControllerPool();

    private List<Class<T>> controllers = ImmutableList.of((Class<T>) UserController.class);
    private final Map<Class<T>, T> pool;

    private ControllerPool() {
        pool = new HashMap<>();
        controllers.forEach(controllerClass -> pool.put(controllerClass, null));
    }

    public static ControllerPool of() {
        return container;
    }

    public Controller search(String pattern) {
        Optional<Class<T>> maybeControllerClass = controllers.stream().filter(clazz -> clazz.getAnnotation(RequestMapping.class).value().startsWith(pattern)).findFirst();
        if (!maybeControllerClass.isPresent()) {
            maybeControllerClass = Optional.of((Class<T>) ViewController.class);
        }
        Class<T> controllerClass = maybeControllerClass.get();
        Controller controller = pool.get(controllerClass);
        if (Objects.isNull(controller)) {
            return create(controllerClass);
        }
        return controller;
    }

    private Controller create(Class<T> controllerClass) {
        try {
            Constructor<T> constructor = controllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            T controller = constructor.newInstance();
            constructor.setAccessible(false);
            pool.put(controllerClass, controller);
            return controller;
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            System.exit(1);
            return null;
        }
    }
}
