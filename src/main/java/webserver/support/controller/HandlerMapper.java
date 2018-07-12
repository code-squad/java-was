package webserver.support.controller;

import webserver.controller.Controller;
import webserver.controller.ControllerPool;

import static util.HandlerMapperUtils.parseControllerName;

public class HandlerMapper {
    private static final ControllerPool controllerPool = ControllerPool.of();

    public static Controller mapHandler(String requestPath) {
        return search(parseControllerName(requestPath));
    }

    private static Controller search(String pattern) {
        return controllerPool.search(pattern);
    }
}
/*
    TODO : 3가지 경우를 커버할 수 있어야함
    /user/abc.html
    /user/create
    /index.html
 */