package com.larry.webserver.mvc.controllerFlow;

import com.larry.webserver.http.RequestHandlerKey;
import com.larry.webserver.http.RequestHandlerValue;

public class ControllerPool implements MethodPool {

    private MethodFinder finder;

    public ControllerPool(MethodFinder finder) {
        this.finder = finder;
    }

    @Override
    public RequestHandlerValue getMethodValue(RequestHandlerKey httpMethodAndPath) {
        return finder.findValue(httpMethodAndPath);
    }
}
