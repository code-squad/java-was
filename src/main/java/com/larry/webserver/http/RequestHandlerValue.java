package com.larry.webserver.http;

import com.larry.webserver.mvc.viewFlow.ModelAndView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class RequestHandlerValue {

    private Class<?> controller;
    private Method method;

    public RequestHandlerValue(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Response execute(Request request, Response response) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InstantiationException {
        ModelAndView modelAndView = (ModelAndView) method.invoke(controller.newInstance(), request, response);
        response.setHttpVersion(request.getHttpVersion());
        response.setView(modelAndView);
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestHandlerValue that = (RequestHandlerValue) o;
        return Objects.equals(controller, that.controller) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {

        return Objects.hash(controller, method);
    }
}
