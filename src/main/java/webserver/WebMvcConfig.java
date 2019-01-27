package webserver;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import setting.ArgumentResolver;
import setting.Controller;
import webserver.handlermapping.HandlerMethodArgumentResolver;

import java.util.*;
import java.util.stream.Stream;

public class WebMvcConfig {

    private Map<Class, HandlerMethodArgumentResolver> handlerMethodArgumentResolvers;

    public WebMvcConfig() {
        this.handlerMethodArgumentResolvers = new HashMap<>();
    }

    public void initHandlerMethodArgumentResolvers() {
        Reflections reflections = new Reflections("webserver");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ArgumentResolver.class);
        for (Class<?> clazz : classes) {
            try {
                HandlerMethodArgumentResolver handlerMethodArgumentResolver = (HandlerMethodArgumentResolver) clazz.newInstance();
                handlerMethodArgumentResolvers.put(handlerMethodArgumentResolver.identification(), handlerMethodArgumentResolver);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public HandlerMethodArgumentResolver obtainHandlerMethodArgumentResolver(Class clazz) {
        if(handlerMethodArgumentResolvers.containsKey(clazz)) {
            return handlerMethodArgumentResolvers.get(clazz);
        }
        return handlerMethodArgumentResolvers.get(Object.class);
    }
}
