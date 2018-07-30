package com.larry.webserver.mvc.controllerFlow;

import com.larry.webserver.annotations.RequestMapping;
import com.larry.webserver.http.HttpMethod;
import com.larry.webserver.http.Request;
import com.larry.webserver.http.Response;
import com.larry.webserver.mvc.viewFlow.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodExecutor {

    private final Logger log = LoggerFactory.getLogger(MethodExecutor.class);

    private Class<?> controller;
    private Method executeMethod;

    public MethodExecutor(Class<?> controller) {
        this.controller = controller;
    }

    public boolean findMethod(Request request) {
        String controllerMappedPath = getControllerPath(controller);
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            String methodPath = "";
            if (method.isAnnotationPresent(RequestMapping.class)) {
                methodPath = method.getDeclaredAnnotation(RequestMapping.class).path();
            }
            String fullPath = controllerMappedPath + methodPath;
            log.info("full path : {}", fullPath);
            if (isMatchedMethod(request, method, fullPath)) {
                executeMethod = method;
                return true;
            }
        }
        return false;
    }

    private boolean isMatchedMethod(Request request, Method method, String fullPath) {
        return fullPath.equals(request.getPath()) && HttpMethod.valueOf(method.getAnnotation(RequestMapping.class).method()).equals(request.getHttpMethod());
    }

    public Response getViewName(Request request) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException {
        Response response = new Response();
        ModelAndView modelAndView = (ModelAndView) executeMethod.invoke(controller.newInstance(), request, response);
        response.setHttpVersion(request.getHttpVersion());
        response.setView(modelAndView);
        return response;
    }

    private String getControllerPath(Class<?> controller) {
        if (controller.isAnnotationPresent(RequestMapping.class)) {
            return controller.getAnnotation(RequestMapping.class).path();
        }
        return "";
    }
}
