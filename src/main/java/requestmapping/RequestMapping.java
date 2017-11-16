package requestmapping;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.Maps;

import controller.Controller;
import controller.ResourceController;
import util.HttpRequestUtils.RequestTypes;

public class RequestMapping {

    private static RequestMapping requestMapping;

    private RequestMapping() {

    }

    public static RequestMapping getInstance() {
        if (requestMapping == null) {
            requestMapping = new RequestMapping();

        }
        return requestMapping;
    }

    private Map<RequestLine, Controller> controllerList = Maps.newHashMap();

    public Controller getController(RequestLine rq) {
        Controller controller = controllerList.get(rq);
        if (controller == null) {
            return new ResourceController();
        }
        return controller;
    }

    public void addController(RequestLine rq, Controller controller) {
        this.controllerList.put(rq, controller);
    }
}
