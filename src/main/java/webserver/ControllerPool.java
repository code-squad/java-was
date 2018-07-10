package webserver;

import com.google.common.collect.ImmutableMap;
import exception.HandlerMappingException;

import java.util.Map;

class ControllerPool {
    private static ControllerPool container = new ControllerPool();

    private final Map<Class<? extends Controller>, ? extends Controller> POOL;

    private ControllerPool() {
        POOL = ImmutableMap.of(
                HomeController.class, HomeController.of(),
                UserController.class, UserController.of()
        );
    }

    static ControllerPool of() {
        return container;
    }

    Controller search(String pattern) {
        Class<? extends Controller> controllerClass = (Class<? extends Controller>) POOL.keySet().stream().filter(clazz -> clazz.getAnnotation(RequestMapping.class).value().contains(pattern)).findFirst().orElseThrow(HandlerMappingException::new);
        return POOL.get(controllerClass);
    }
}
