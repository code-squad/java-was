package com.larry.webserver.mvc.viewFlow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ModelAndView<E> {

    private final Logger log = LoggerFactory.getLogger(ModelAndView.class);

    private List<Model> models = new ArrayList<>();

    private String viewName;

    private ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(String name, List<E> elems) {
        Model model = new Model(name, elems);
        this.models.add(model);
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public static ModelAndView viewOf(String viewName) {
        return new ModelAndView(viewName);
    }

    public String getViewName() {
        return viewName;
    }

    public byte[] resolveBody() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return new ViewResolver().resolve(viewName, models);
    }

    public String getRedirectUrl() {
        viewName = viewName.replace("redirect:/", "");
        return viewName;
    }

    public boolean isRedirect() {
        return viewName.startsWith("redirect:/");
    }
}
