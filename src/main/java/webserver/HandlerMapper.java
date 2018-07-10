package webserver;

import static util.HandlerMapperUtils.parseControllerName;

class HandlerMapper {
    private static final ControllerPool controllerPool = ControllerPool.of();

    static Controller mapHandler(String requestPath) {
        return search(parseControllerName(requestPath));
    }

    private static Controller search(String pattern) {
        return controllerPool.search(pattern);
    }
}
