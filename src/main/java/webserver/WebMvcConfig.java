package webserver;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import setting.ArgumentResolver;
import setting.Controller;
import webserver.handlermapping.CustomObjectMethodArgumentResolver;
import webserver.handlermapping.HandlerMethodArgumentResolver;

import java.util.*;
import java.util.stream.Stream;

public class WebMvcConfig {

    private List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers;

    public WebMvcConfig() {
        this.handlerMethodArgumentResolvers = new ArrayList<>();
    }

    public void initHandlerMethodArgumentResolvers() {
        Reflections reflections = new Reflections("webserver");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ArgumentResolver.class);
        for (Class<?> clazz : classes) {
            try {
                handlerMethodArgumentResolvers.add((HandlerMethodArgumentResolver) clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public HandlerMethodArgumentResolver obtainHandlerMethodArgumentResolver(Class clazz) throws InstantiationException, IllegalAccessException {
        for (HandlerMethodArgumentResolver handlerMethodArgumentResolver : handlerMethodArgumentResolvers) {
            if(handlerMethodArgumentResolver.supportsParameter(clazz)) {
                return handlerMethodArgumentResolver;
            }
        }

        return new CustomObjectMethodArgumentResolver();
    }
}
