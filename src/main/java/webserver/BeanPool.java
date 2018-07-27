package webserver;

import java.util.Set;

public interface BeanPool {

    Set<Class<?>> getControllers();
}
