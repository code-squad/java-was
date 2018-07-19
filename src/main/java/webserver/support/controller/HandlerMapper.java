package webserver.support.controller;

import webserver.controller.Controller;
import webserver.controller.ControllerPool;
import webserver.request.Request;

public class HandlerMapper {
    private static final ControllerPool controllerPool = ControllerPool.of();

    public static Controller mapHandler(Request request) {
        return controllerPool.search(request);
    }
}