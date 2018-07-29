package com.larry.webserver;

import java.io.IOException;
import java.util.Map;

public class ModelAndView {

    private Map<String, Object> model;

    private String viewName;

    private ModelAndView(Map<String, Object> model, String viewName) {
        this.model = model;
        this.viewName = viewName;
    }

    private ModelAndView(String viewName) {
        this(null, viewName);
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

    public byte[] resolveBody() throws IOException {
        return ViewResolver.resolve(viewName, model);
    }
}
